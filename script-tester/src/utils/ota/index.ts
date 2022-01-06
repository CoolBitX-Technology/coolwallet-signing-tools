import axios, { Method } from 'axios';
import { apdu, transport as Transport } from '@coolwallet/core';
import * as TEST from '@/configs/scripts/test';
import { getAPIOption, formatAPIResponse } from './options';
import { insertScript, insertDeleteScript, insertLoadScript } from './scripts';

const CHALLENGE_URL = 'https://ota.cbx.io/sdk/challenge';
const CRYPTOGRAM_URL = 'https://ota.cbx.io/sdk/cryptogram';
const CARDMANAGER_AID = 'A000000151000000';
const SSD_AID = 'A000000151535041';

const otaUpdate = async (
  transport: Transport.default,
  secret: string,
  cardId: string,
  progressCallback: (progress: number) => void
): Promise<void> => {
  const script = TEST;

  let progressIndex = 0;
  const progressNum = [28, 36, 44, 50, 88, 100];

  // 28
  progressCallback(progressNum[progressIndex]);
  progressIndex += 1;
  await apdu.ota.selectApplet(transport, CARDMANAGER_AID);
  await apdu.ota.selectApplet(transport, SSD_AID);

  // 36
  console.debug('mutual Authorization Start----');
  progressCallback(progressNum[progressIndex]);
  progressIndex += 1;
  const { method, headers, body } = await getAPIOption(secret, cardId);
  const challengeResponse = await axios({ url: CHALLENGE_URL, method: method as Method, headers, data: body });
  console.debug('cardID: ', cardId);
  const challengeObj = await formatAPIResponse(transport, challengeResponse?.data);
  const {
    method: challenge_method,
    headers: challenge_headers,
    body: challenge_body,
  } = await getAPIOption(secret, cardId, challengeObj.outputData);
  const cryptogramResponse = await axios({
    url: CRYPTOGRAM_URL,
    method: challenge_method as Method,
    headers: challenge_headers,
    data: challenge_body,
  });
  await formatAPIResponse(transport, cryptogramResponse.data);
  console.debug('mutual Authorization Done----');

  // 44
  progressCallback(progressNum[progressIndex]);
  progressIndex += 1;
  await insertDeleteScript(transport, script.deleteScript);
  console.debug('Delete Card Manager Done');

  // 50 ~ 88
  await insertLoadScript(
    transport,
    script.loadScript,
    progressCallback,
    progressNum[progressIndex],
    progressNum[++progressIndex]
  );
  console.debug('Load OTA Script Done');

  await insertScript(transport, script.installScript);

  await apdu.mcu.display.hideUpdate(transport); // Hide update from the card

  await apdu.ota.selectApplet(transport, CARDMANAGER_AID);
  const { status: updateSuccess } = await apdu.ota.selectApplet(transport);
  console.debug(`updateSuccess: ${updateSuccess}`);

  // 100
  progressCallback(progressNum[++progressIndex]);
};

export { otaUpdate };

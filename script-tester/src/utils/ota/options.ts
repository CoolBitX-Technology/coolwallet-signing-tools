import { SignJWT } from 'jose';
import jwt_decode from 'jwt-decode';
import isEmpty from 'lodash/isEmpty';
import { Transport, apdu } from '@coolwallet/core';

interface APIOptions {
  body: {
    keyNum: string;
    payload: string;
  };
  method: string;
  headers: {
    Accept: string;
    'Content-Type': string;
    'auth-id': string;
  };
}

export type CommandType = {
  CLA: string;
  INS: string;
  P1: string | undefined;
  P2: string | undefined;
  packets: string;
};

/**
 *
 * @param data
 */
const getAPIOption = async (secret: string, cardId: string, challengeData = ''): Promise<APIOptions> => {
  let data;

  if (isEmpty(challengeData)) {
    data = { cwid: cardId };
  } else {
    data = { cryptogram: challengeData, cwid: cardId };
  }

  console.debug(data);

  const iat = Math.floor(Date.now() / 1000);

  const payload = await new SignJWT(data)
    .setProtectedHeader({ alg: 'HS256', typ: 'JWT' })
    .setIssuedAt(iat)
    .setExpirationTime(iat + 60 * 60 * 24)
    .sign(Buffer.from(secret, 'utf-8'));

  console.debug(`payload: ${payload}`);

  const body = {
    keyNum: '1',
    payload,
  };

  return {
    body,
    method: 'POST',
    headers: {
      Accept: 'application/json',
      'Content-Type': 'application/json',
      'auth-id': cardId,
    },
  };
};

const formatAPIResponse = async (
  transport: Transport,
  result: any
): ReturnType<typeof apdu.execute.executeCommand> => {
  const obj = jwt_decode(result.cryptogram);
  console.debug(`Server Auth Response : ${JSON.stringify(obj)}`);
  const { CLA, INS, P1, P2, packets } = obj as CommandType;

  return apdu.execute.executeCommand(transport, { CLA, INS, P1, P2 }, 'SE', packets);
};

export { getAPIOption, formatAPIResponse };

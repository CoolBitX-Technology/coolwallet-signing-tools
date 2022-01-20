import { apdu, Transport } from '@coolwallet/core';
import { commands } from '@/configs/command';
import { SDKError } from '@coolwallet/core/lib/error';

const executeScript = async (
  transport: Transport,
  appId: string,
  appPrivKey: string,
  argument: string
): Promise<string> => {
  const encryptedSignature = await apdu.tx.executeScript(transport, appId, appPrivKey, argument);
  console.log('encryptedSignature: ', encryptedSignature);
  return encryptedSignature ?? '';
};

const executeSegmentScript = async (transport: Transport, argument: string): Promise<string> => {
  const limit = 3094;
  let packetCount = 1;
  const total = Math.ceil(argument.length / 3094);
  while (packetCount <= total) {
    const chunk = argument.substring((packetCount - 1) * limit, packetCount * limit);
    const p1 = ('' + packetCount + '').padStart(2, '0');
    const p2 = ('' + total + '').padStart(2, '0');
    const { outputData, statusCode, msg } = await apdu.execute.executeCommand(
      transport,
      commands.EXECUTE_SEGMENT_SCRIPT,
      'SE',
      chunk,
      p1,
      p2
    );
    console.log('outputData: ', outputData);
    console.log('statusCode: ', statusCode);
    console.log('msg: ', msg);
    if (statusCode !== '9000') {
      throw new SDKError(executeSegmentScript.name, 'Cannot send segment data');
    }
    if (total === packetCount) return outputData;
    console.log('Remaining data:', parseInt(outputData, 16));
    packetCount += 1;
  }

  throw new SDKError(executeSegmentScript.name, 'Segment data length wrong');
};

/**
 * Scriptable step 3
 * @param {*} transport
 * @param {*} argument
 * @param {*} redeemScriptType (redeemScriptType === ScriptType.P2PKH) ? "10" : "11"
 */
const executeUtxoScript = async (transport: Transport, argument: string) => {
  const {
    outputData: encryptedSignature,
    statusCode,
    msg,
  } = await apdu.execute.executeCommand(transport, commands.EXECUTE_UTXO_SCRIPT, 'SE', argument, '11', undefined);

  console.log('encryptedSignature: ', encryptedSignature);
  console.log('statusCode: ', statusCode);
  console.log('msg: ', msg);
  return encryptedSignature;
};

export { executeScript, executeSegmentScript, executeUtxoScript };

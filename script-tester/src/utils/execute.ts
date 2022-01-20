import { apdu, Transport } from '@coolwallet/core';
import { commands } from '@/configs/command';

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

export { executeScript, executeUtxoScript };

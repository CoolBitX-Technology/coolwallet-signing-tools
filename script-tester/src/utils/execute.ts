import { apdu, transport as Transport } from '@coolwallet/core';
import { commands } from '@/configs/command';

const executeScript = async (transport: Transport.default, argument: string): Promise<string> => {
  const {
    outputData: encryptedSignature,
    statusCode,
    msg,
  } = await apdu.execute.executeCommand(transport, commands.EXECUTE_SCRIPT, 'SE', argument, undefined, undefined);
  console.log('encryptedSignature: ', encryptedSignature);
  console.log('statusCode: ', statusCode);
  console.log('msg: ', msg);
  return encryptedSignature;
};

/**
 * Scriptable step 3
 * @param {*} transport
 * @param {*} argument
 * @param {*} redeemScriptType (redeemScriptType === ScriptType.P2PKH) ? "10" : "11"
 */
const executeUtxoScript = async (transport: Transport.default, argument: string) => {
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

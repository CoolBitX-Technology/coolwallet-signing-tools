import React, { useState, useEffect } from 'react';
import { Container, Row } from 'react-bootstrap';
import { makeSendComponentWithTwoInput, makeSendComponentWithThreeInput } from './componentMaker';
import { apdu } from '@coolwallet/core';
import { commands } from "../../config/command";
const bip39 = require('bip39');

function Signing({ transport, appId, appPrivateKey, isLocked, setIsLocked }) {
  // const [mnemonic, setMnemonic] = useState('');
  // const [recoverStatus, setRecoverStatus] = useState('');

  const [argument, setArgument] = useState('');
  const [script, setScript] = useState('');
  const [Signature, setSignature] = useState('');

  const [utxoIntputArgument, setUtxoIntputArgument] = useState([]);
  const [utxoOutputArgument, setUtxoOutputArgument] = useState('');
  const [utxoScript, setUtxoScript] = useState('');
  const [utxoSignature, setUtxoSignature] = useState('');
  const scriptSingature = "FA0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"

  const signTx = async () => {
    setIsLocked(true)
    try {

      const fullSctipt = script + scriptSingature

      const preActions = [];
      const sendScript = async () => {
        await apdu.tx.sendScript(transport, fullSctipt);
      };

      preActions.push(sendScript);

      const action = async () => executeScript(transport, argument);

      if (preActions) {
        // eslint-disable-next-line no-await-in-loop
        for (const preAction of preActions) {
          await preAction();
        }
      }

      const encryptedSignature = await action();

      // finish prepare
      await apdu.tx.finishPrepare(transport);

      // get tx detail
      await apdu.tx.getTxDetail(transport)

      await apdu.tx.clearTransaction(transport);
      await apdu.mcu.control.powerOff(transport);
      
      setSignature(encryptedSignature)
      
    } catch (error) {

      console.error(error);
    } finally {
      setIsLocked(false);
    }
  }


  const signUtxoTx = async () => {
    setIsLocked(true)
    try {
      console.log("utxoIntputArgument: ", utxoIntputArgument)
      console.log("utxoOutputArgument: ", utxoOutputArgument)

      const fullSctipt = utxoScript + scriptSingature

      console.log("fullSctipt: ", fullSctipt)

      await apdu.tx.sendScript(transport, fullSctipt);

      await executeScript(
        transport,
        utxoOutputArgument
      )

      // const actions = utxoIntputArgument.map(
      //   (utxoArgument) => async () => {
      //     return executeUtxoScript(transport, appId, appPrivateKey, await utxoArgument);
      //   });


      const encryptedSignatureArray = await executeUtxoScript(transport, utxoIntputArgument);

      // const encryptedSignatureArray = [];
      // eslint-disable-next-line no-await-in-loop
      // for (const action of actions) {
      //   encryptedSignatureArray.push(await action());
      // }

      // encryptedSignatureArray.push(await actions());
      
      console.log("encryptedSignatureArray: ", encryptedSignatureArray)

      
      // finish prepare
      await apdu.tx.finishPrepare(transport);

      // get tx detail
      await apdu.tx.getTxDetail(transport)

      await apdu.tx.clearTransaction(transport);
      await apdu.mcu.control.powerOff(transport);

      setUtxoSignature(encryptedSignatureArray)
    } catch (error) {

      console.error(error);
    } finally {
      setIsLocked(false);
    }
  }


  const executeScript = async (transport, argument) => {
    const { outputData: encryptedSignature, statusCode, msg } = await apdu.execute.executeCommand(
      transport,
      commands.EXECUTE_SCRIPT,
      'SE',
      argument,
      undefined,
      undefined,
    );
    console.log("encryptedSignature: ", encryptedSignature)
    console.log("statusCode: ", statusCode)
    console.log("msg: ", msg)
    return encryptedSignature;
  };


  /**
   * Scriptable step 3
   * @param {*} transport
   * @param {*} argument
   * @param {*} redeemScriptType (redeemScriptType === ScriptType.P2PKH) ? "10" : "11"
   */
  const executeUtxoScript = async (transport, utxoArgument) => {
    const { outputData: encryptedSignature, statusCode, msg } = await apdu.execute.executeCommand(
      transport,
      commands.EXECUTE_UTXO_SCRIPT,
      'SE',
      utxoArgument,
      "11",
      undefined
    );

    console.log("encryptedSignature: ", encryptedSignature)
    console.log("statusCode: ", statusCode)
    console.log("msg: ", msg)
    return encryptedSignature;
  };

  const disabled = !transport || isLocked;
  // (title, content, onClick, disabled, input, setInput, placeholder, input2, setInput2, placeholder2)
  return (
    <Container>
      <Row className='title2'>
        <p>Scriptable Signing</p>
      </Row>
      {makeSendComponentWithTwoInput('Signature', Signature, signTx, disabled, argument, setArgument, 'argument', script, setScript, 'script')}
      {makeSendComponentWithThreeInput('UTXO Signature', utxoSignature, signUtxoTx, disabled, utxoIntputArgument, setUtxoIntputArgument, 'intput arg', utxoOutputArgument, setUtxoOutputArgument, 'output arg', utxoScript, setUtxoScript, 'script')}
    </Container>
  );
}

export default Signing;

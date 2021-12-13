import { useState, useContext, ReactElement } from 'react';
import { Container, Row } from 'react-bootstrap';
import clsx from 'clsx';
import isNil from 'lodash/isNil';
import padEnd from 'lodash/padEnd';
import { apdu, transport as Transport } from '@coolwallet/core';
import { ButtonInputs } from '@/components';
import { executeScript, executeUtxoScript } from '@/utils/execute';
import Context from '@/store';

const SIGNATURE = padEnd('FA', 144, '0');

const row = clsx('border-t-2', 'border-gray-400', 'mt-4', 'pt-4', 'text-center', 'text-2xl');

interface Props {
  transport: Transport.default | null;
  appId: string | null;
  appPrivateKey: string;
}

const Signing = (props: Props): ReactElement => {
  const { connected, isLocked, setIsLocked } = useContext(Context);

  const [argument, setArgument] = useState('');
  const [script, setScript] = useState('');
  const [Signature, setSignature] = useState('');

  const [utxoInputArgument, setUtxoInputArgument] = useState('');
  const [utxoOutputArgument, setUtxoOutputArgument] = useState('');
  const [utxoScript, setUtxoScript] = useState('');
  const [utxoSignature, setUtxoSignature] = useState('');

  const signTx = async () => {
    if (isNil(props.transport)) return;
    setIsLocked(true);
    try {
      const fullScript = script + SIGNATURE;

      console.log('fullScript: ', fullScript);

      await apdu.tx.sendScript(props.transport, fullScript);

      const encryptedSignature = await executeScript(props.transport, argument);

      // finish prepare
      await apdu.tx.finishPrepare(props.transport);

      // get tx detail
      await apdu.tx.getTxDetail(props.transport);

      await apdu.tx.clearTransaction(props.transport);
      await apdu.mcu.control.powerOff(props.transport);

      setSignature(encryptedSignature);
    } catch (error) {
      console.error(error);
    } finally {
      setIsLocked(false);
    }
  };

  const signUtxoTx = async () => {
    if (isNil(props.transport)) return;
    setIsLocked(true);
    try {
      console.log('utxoIntputArgument: ', utxoInputArgument);
      console.log('utxoOutputArgument: ', utxoOutputArgument);

      const fullScript = utxoScript + SIGNATURE;

      console.log('fullScript: ', fullScript);

      await apdu.tx.sendScript(props.transport, fullScript);

      await executeScript(props.transport, utxoOutputArgument);

      const encryptedSignatureArray = await executeUtxoScript(props.transport, utxoInputArgument);

      console.log('encryptedSignatureArray: ', encryptedSignatureArray);

      // finish prepare
      await apdu.tx.finishPrepare(props.transport);

      // get tx detail
      await apdu.tx.getTxDetail(props.transport);

      await apdu.tx.clearTransaction(props.transport);
      await apdu.mcu.control.powerOff(props.transport);

      setUtxoSignature(encryptedSignatureArray);
    } catch (error) {
      console.error(error);
    } finally {
      setIsLocked(false);
    }
  };

  const disabled = !connected || isLocked;
  // (title, content, onClick, disabled, input, setInput, placeholder, input2, setInput2, placeholder2)
  return (
    <Container>
      <Row className={row}>
        <p>Scriptable Signing</p>
      </Row>
      <ButtonInputs
        title="Signature"
        content={Signature}
        onClick={signTx}
        disabled={disabled}
        inputs={[
          {
            value: argument,
            onChange: setArgument,
            placeholder: 'argument',
          },
          {
            value: script,
            onChange: setScript,
            placeholder: 'script',
          },
        ]}
      />
      <ButtonInputs
        title="UTXO Signature"
        content={utxoSignature}
        onClick={signUtxoTx}
        disabled={disabled}
        inputs={[
          {
            value: utxoInputArgument,
            onChange: setUtxoInputArgument,
            placeholder: 'input arg',
          },
          {
            value: utxoOutputArgument,
            onChange: setUtxoOutputArgument,
            placeholder: 'output arg',
          },
          {
            value: utxoScript,
            onChange: setUtxoScript,
            placeholder: 'script',
          },
        ]}
      />
    </Container>
  );
};

export default Signing;

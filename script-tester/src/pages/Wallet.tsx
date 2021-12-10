import { useState, useContext, ReactElement } from 'react';
import { Container, Row } from 'react-bootstrap';
import clsx from 'clsx';
import isNil from 'lodash/isNil';
import { mnemonicToSeedSync } from 'bip39';
import { apdu, transport as Transport } from '@coolwallet/core';
import { ButtonInputs } from '@/components';
import Context from '@/store';

const row = clsx('border-t-2', 'border-gray-400', 'mt-4', 'pt-4', 'text-center', 'text-2xl');

interface Props {
  transport: Transport.default | null;
  appId: string | null;
  appPrivateKey: string;
}

const Wallet = (props: Props): ReactElement => {
  const { connected, setIsLocked, isLocked } = useContext(Context);
  const [mnemonic, setMnemonic] = useState('');
  const [recoverStatus, setRecoverStatus] = useState('');

  const setSeed = async () => {
    if (isNil(props.transport)) return;
    setRecoverStatus('recovering');
    setIsLocked(true);
    try {
      const seedHex = mnemonicToSeedSync(mnemonic).toString('hex');
      console.log('seedHex: ' + seedHex);
      const SEPublicKey = localStorage.getItem('SEPublicKey');
      if (isNil(SEPublicKey) || isNil(props.appId)) return;
      console.log(`SEPublicKey: ${SEPublicKey}`);
      await apdu.wallet.setSeed(props.transport, props.appId, props.appPrivateKey, seedHex, SEPublicKey);
      setRecoverStatus('success');
    } catch (error) {
      console.error(error);
    } finally {
      setIsLocked(false);
    }
  };

  const disabled = !connected || isLocked;

  return (
    <Container>
      <Row className={row}>
        <p>Wallet</p>
      </Row>
      <ButtonInputs
        title="Set mnemonic"
        content={recoverStatus}
        onClick={setSeed}
        disabled={disabled}
        btnTitle="Send"
        inputs={[{ value: mnemonic, onChange: setMnemonic, placeholder: 'mnemonic words', xs: 6 }]}
      />
    </Container>
  );
};

Wallet.defaultProps = {
  appId: '',
};

export default Wallet;

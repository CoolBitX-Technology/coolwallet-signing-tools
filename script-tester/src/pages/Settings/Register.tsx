import { useState, ReactElement, useContext } from 'react';
import { Container } from 'react-bootstrap';
import isNil from 'lodash/isNil';
import { apdu, transport as Transport } from '@coolwallet/core';
import Button from '@/components/Button';
import Context from '@/store';

interface Props {
  transport: Transport.default | null;
  appPublicKey: string;
  setAppId(value: string): void;
}

const Register = (props: Props): ReactElement => {
  const { connected, isLocked } = useContext(Context);
  const [isResetting, setIsResetting] = useState('');
  const [isRegistering, setIsRegistering] = useState('');

  const resetCard = async () => {
    if (isNil(props.transport)) return;
    setIsResetting('resetting, please press button');
    try {
      await apdu.general.resetCard(props.transport);
    } catch (error) {
      console.error(error);
    } finally {
      setIsResetting('done');
    }
  };

  const registerCard = async () => {
    if (isNil(props.transport)) return;
    setIsRegistering('registering, please press button');
    try {
      const name = 'TestAPP';
      const SEPublicKey = localStorage.getItem('SEPublicKey');
      if (isNil(SEPublicKey)) return;
      const password = '12345678';
      const appId = await apdu.pair.register(props.transport, props.appPublicKey, password, name, SEPublicKey);
      localStorage.setItem('appId', appId);
      props.setAppId(appId);
    } catch (error) {
      // TODO
      // if (error instanceof Error.AlreadyRegistered) {
      //   console.log(`Already registered`);
      // } else {
      console.error(error);
      // }
    } finally {
      setIsRegistering('done');
    }
  };

  const disabled = !connected || isLocked;

  return (
    <Container>
      <Button title="Reset" content={isResetting} onClick={resetCard} disabled={disabled} />
      <Button title="Register" content={isRegistering} onClick={registerCard} disabled={disabled} />
    </Container>
  );
};

export default Register;

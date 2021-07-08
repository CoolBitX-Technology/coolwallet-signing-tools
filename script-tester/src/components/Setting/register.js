import React, { useState, useEffect } from 'react';
import { Container } from 'react-bootstrap';
import { makeGetComponent } from '../../utils/componentMaker';
import { apdu } from '@coolwallet/core';

function Register({ transport, appPublicKey, isLocked, setIsLocked }) {
  const [isResetting, setIsResetting] = useState('')
  const [isRegistering, setIsRegistering] = useState('')

  const resetCard = async () => {
    setIsResetting('resetting, please press button')
    try {
      await apdu.general.resetCard(transport);
    } catch (error) {
      console.error(error)
    } finally {
      setIsResetting('done')
    }

  }

  const registerCard = async () => {

    setIsRegistering('registering, please press button')
    try {
      const name = 'TestAPP'
      const SEPublicKey = localStorage.getItem('SEPublicKey')
      const password = '12345678'
      const appId = await apdu.pair.register(transport, appPublicKey, password, name, SEPublicKey);
      localStorage.setItem('appId', appId);
    } catch (error) {
      // TODO
      // if (error instanceof Error.AlreadyRegistered) {
      //   console.log(`Already registered`);
      // } else {
      console.error(error);
      // }
    } finally {
      setIsRegistering('done')
    }
  };

  const disabled = !transport || isLocked;

  return (
    <Container>
      {makeGetComponent('Reset', isResetting, resetCard, disabled)}
      {makeGetComponent('Register', isRegistering, registerCard, disabled)}
    </Container>
  );
}

export default Register;

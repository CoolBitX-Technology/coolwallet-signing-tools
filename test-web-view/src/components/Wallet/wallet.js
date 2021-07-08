import React, { useState, useEffect } from 'react';
import { Container, Row } from 'react-bootstrap';
import { makeGetComponent, makeSendComponent } from '../../utils/componentMaker';
import { apdu, utils } from '@coolwallet/core';
const bip39 = require('bip39');

function Wallet({ transport, appId, appPrivateKey, isLocked, setIsLocked }) {
  const [mnemonic, setMnemonic] = useState('');
  const [recoverStatus, setRecoverStatus] = useState('');

  const setSeed = async () => {
    setRecoverStatus('recovering')
    setIsLocked(true)
    try {
      const seedHex = bip39.mnemonicToSeedSync(mnemonic).toString('hex');
      console.log("seedHex: " + seedHex);
      const SEPublicKey = localStorage.getItem('SEPublicKey')

      console.log(`SEPublicKey: ${SEPublicKey}`);
      await apdu.wallet.setSeed(transport, appId, appPrivateKey, seedHex, SEPublicKey)
      setRecoverStatus('success')
      
    } catch (error) {
      
      console.error(error);
    } finally {
      setIsLocked(false);
    }
  }

  const disabled = !transport || isLocked;

  return (
    <Container>
      <Row className='title2'>
        <p>Wallet</p>
      </Row>
      {makeSendComponent('Set seed', recoverStatus, setSeed, disabled, mnemonic, setMnemonic, 'data')}
    </Container>
  );
}

export default Wallet;

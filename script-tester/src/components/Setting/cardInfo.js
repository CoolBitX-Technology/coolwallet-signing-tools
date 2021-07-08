import React, { useState, useEffect } from 'react';
import { Container } from 'react-bootstrap';
import { makeGetComponent } from '../../utils/componentMaker';
import { apdu } from '@coolwallet/core';

function CardInfo({ transport, isLocked, setIsLocked }) {
  const [isAppletExist, setIsAppletExist] = useState('');
  const [cardInfo, setCardInfo] = useState('');
  const [MCUStatus, setMCUStatus] = useState('');
  const [SEVersion, setSEVersion] = useState('');

  useEffect(() => {
    if (!transport) {
      setCardInfo('');
      setMCUStatus('');
      setIsAppletExist('');
      setSEVersion('');
    }
  }, [transport]);

  const checkApplet = async () => {
    setIsLocked(true)
    try {
      const { status } = await apdu.ota.selectApplet(transport);
      console.log('isAppletExist :', status);
      setIsAppletExist(status.toString());
    } catch (error) {
      setIsAppletExist(error.message);
      console.error(error);
    } finally {
      setIsLocked(false);
    }
  }

  const getCardInfo = async () => {
    setIsLocked(true);
    try {
      const data = await apdu.info.getCardInfo(transport);
      const cardInfo = `paired: ${data.paired}, locked: ${data.locked}, walletCreated: ${data.walletCreated},showDetail: ${data.showDetail}, pairRemainTimes: ${data.pairRemainTimes}`;
      setCardInfo(cardInfo);
    } catch (error) {
      setCardInfo(error.message);
      console.error(error);
    } finally {
      setIsLocked(false);
    }
  }

  const getMCUStatus = async () => {
    setIsLocked(true)
    try {
      const data = await apdu.mcu.dfu.getMCUVersion(transport)
      const cardInfo = `MCUStatus: ${data.fwStatus}, cardMCUVersion: ${data.cardMCUVersion}`;
      setMCUStatus(cardInfo)
    } catch (error) {
      setMCUStatus(error.message)
      console.error(error)
    } finally {
      setIsLocked(false)
    }
  }

  const getSEVersion = async () => {
    setIsLocked(true)
    try {
      const data = await apdu.general.getSEVersion(transport)
      setSEVersion(data)
    } catch (error) {
      setSEVersion(error.message)
      console.error(error)
    } finally {
      setIsLocked(false)
    }
  }

  const disabled = !transport || isLocked;

  return (
    <Container>
      {makeGetComponent('SE Version', SEVersion, getSEVersion, disabled)}
    </Container>
  );
}

export default CardInfo;

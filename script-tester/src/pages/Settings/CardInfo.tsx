import { useState, useEffect, FC, useContext } from 'react';
import { Container } from 'react-bootstrap';
import isNil from 'lodash/isNil';
import { apdu, Transport } from '@coolwallet/core';
import Button from '@/components/Button';
import Context from '@/store';

interface Props {
  transport: Transport | null;
}

const CardInfo: FC<Props> = (props: Props) => {
  const { connected, isLocked, setIsLocked } = useContext(Context);
  const [isAppletExist, setIsAppletExist] = useState('');
  const [cardInfo, setCardInfo] = useState('');
  const [MCUStatus, setMCUStatus] = useState('');
  const [SEVersion, setSEVersion] = useState('');

  useEffect(() => {
    // If card is disconnected reset all state.
    if (!connected) {
      setCardInfo('');
      setMCUStatus('');
      setIsAppletExist('');
      setSEVersion('');
    }
  }, [connected]);

  const checkApplet = async () => {
    if (isNil(props.transport)) return;
    setIsLocked(true);
    try {
      const { status } = await apdu.ota.selectApplet(props.transport);
      console.log('isAppletExist :', status);
      setIsAppletExist(status.toString());
    } catch (e) {
      const error = e as Error;
      setIsAppletExist(error.message);
      console.error(error);
    } finally {
      setIsLocked(false);
    }
  };

  const getCardInfo = async () => {
    if (isNil(props.transport)) return;
    setIsLocked(true);
    try {
      const data = await apdu.info.getCardInfo(props.transport);
      const cardInfo = `paired: ${data.paired}, locked: ${data.locked}, walletCreated: ${data.walletCreated},showDetail: ${data.showDetail}, pairRemainTimes: ${data.pairRemainTimes}`;
      setCardInfo(cardInfo);
    } catch (e) {
      const error = e as Error;
      setCardInfo(error.message);
      console.error(error);
    } finally {
      setIsLocked(false);
    }
  };

  const getMCUStatus = async () => {
    if (isNil(props.transport)) return;
    setIsLocked(true);
    try {
      const data = await apdu.mcu.dfu.getMCUVersion(props.transport);
      const cardInfo = `MCUStatus: ${data.fwStatus}, cardMCUVersion: ${data.cardMCUVersion}`;
      setMCUStatus(cardInfo);
    } catch (e) {
      const error = e as Error;
      setMCUStatus(error.message);
      console.error(error);
    } finally {
      setIsLocked(false);
    }
  };

  const getSEVersion = async () => {
    if (isNil(props.transport)) return;
    setIsLocked(true);
    try {
      const data = await apdu.general.getSEVersion(props.transport);
      setSEVersion('' + data + '');
    } catch (e) {
      const error = e as Error;
      setSEVersion(error.message);
      console.error(error);
    } finally {
      setIsLocked(false);
    }
  };

  const disabled = !connected || isLocked;

  return (
    <Container>
      <Button title="SE Version" content={SEVersion} disabled={disabled} onClick={getSEVersion} />
    </Container>
  );
};

export default CardInfo;

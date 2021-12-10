import { useState, ReactElement, useContext } from 'react';
import { Container } from 'react-bootstrap';
import isNil from 'lodash/isNil';
import { apdu, transport as Transport } from '@coolwallet/core';
import { otaUpdate } from '@/utils/ota';
import { ButtonInputs } from '@/components';
import Context from '@/store';

interface Props {
  transport: Transport.default | null;
  appPublicKey: string;
  cardName: string;
}

const OTAUpdate = (props: Props): ReactElement => {
  const { connected, isLocked, setIsLocked } = useContext(Context);
  const [secret, setSecret] = useState('');
  const [progress, setProgress] = useState(0);

  const onUpdate = async () => {
    if (isNil(props.transport)) return;
    setProgress(0);
    setIsLocked(true);
    try {
      const cardId = props.cardName.split(' ')[1];
      await apdu.mcu.display.showUpdate(props.transport);
      await otaUpdate(props.transport, secret, cardId, setProgress);
    } catch (error) {
      console.error(error);
    } finally {
      setProgress(100);
      setIsLocked(false);
    }
  };

  const disabled = !connected || isLocked;

  return (
    <Container>
      <ButtonInputs
        title="Update"
        content={'' + progress + ''}
        onClick={onUpdate}
        disabled={disabled}
        inputs={[{ value: secret, onChange: setSecret, placeholder: 'token', xs: 6 }]}
      />
    </Container>
  );
};

export default OTAUpdate;

import { useRef, useState, FC } from 'react';
import { HashRouter as Router, Route, Redirect } from 'react-router-dom';
import { Container, Row } from 'react-bootstrap';
import clsx from 'clsx';
import WebBleTransport from '@coolwallet/transport-web-ble';
import { Transport, config } from '@coolwallet/core';
import { getAppKeysOrGenerate, getAppIdOrNull } from '@/utils/keypair';
import Context from '@/store';
import Button from '@/components/Button';
import { CardInfo, Register, Wallet, Signing, OTAUpdate } from '@/pages';

const app = clsx(
  'h-screen',
  'flex',
  'flex-col',
  'items-center',
  'pt-4',
  'text-4xl',
  'text-center',
  'text-white',
  'font-sans'
);

const { appPublicKey, appPrivateKey } = getAppKeysOrGenerate();

const App: FC = () => {
  const transport = useRef<Transport | null>(null);
  const [cardName, setCardName] = useState('');
  const [isLocked, setIsLocked] = useState(false);
  const [connected, setConnected] = useState(false);
  const [appId, setAppId] = useState(getAppIdOrNull());

  const connect = async () => {
    const device = await WebBleTransport.listen();
    const cardName = device.name ?? '';
    localStorage.setItem('cardName', cardName);
    setCardName(cardName);

    const webBleTransport = await WebBleTransport.connect(device);
    transport.current = webBleTransport;
    setConnected(true);

    try {
      const SEPublicKey = await config.getSEPublicKey(webBleTransport);
      localStorage.setItem('SEPublicKey', SEPublicKey);
      console.debug(`SEPublicKey: ${SEPublicKey}`);
    } catch(err) {
      console.log('err :', err);
    }
  };

  const disconnect = () => {
    WebBleTransport.disconnect();
    transport.current = null;
    setCardName('');
    setConnected(false);
  };

  return (
    <Context.Provider value={{ connected, setConnected, isLocked, setIsLocked }}>
      <div className={app}>
        <Router>
          <Container>
            <Row className="title">
              <p>CoolWallet Script Tester</p>
            </Row>
            {connected ? (
              <Button title="Card Name" content={cardName} btnTitle="Disconnect" onClick={disconnect} />
            ) : (
              <Button title="Card Name" content={cardName} btnTitle="Connect" onClick={connect} />
            )}
          </Container>
          <Router>
            <Redirect from="/" to="/setting" />
            <Route path="/setting/">
              <CardInfo transport={transport.current} />
              <Register appPublicKey={appPublicKey} transport={transport.current} setAppId={setAppId} />
              <OTAUpdate transport={transport.current} cardName={cardName} appPublicKey={appPrivateKey} />
              <Wallet transport={transport.current} appId={appId} appPrivateKey={appPrivateKey} />
              <Signing transport={transport.current} appId={appId} appPrivateKey={appPrivateKey} />
            </Route>
          </Router>
        </Router>
      </div>
    </Context.Provider>
  );
};

export default App;

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

  const connect = () => {
    WebBleTransport.listen()
      .then((device) => {
        console.debug(device);
        if (!device) throw Error('cant connect');
        const cardName = device.name || '';
        const action = async () => {
          const webBleTransport = await WebBleTransport.connect(device);
          const SEPublicKey = await config.getSEPublicKey(webBleTransport);
          transport.current = webBleTransport;
          setCardName(cardName);
          setConnected(true);
          localStorage.setItem('cardName', cardName);
          localStorage.setItem('SEPublicKey', SEPublicKey);
          console.debug(`SEPublicKey: ${SEPublicKey}`);
        };
        action();
      })
      .catch((error) => console.log(error));
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

import React from "react";
import { HashRouter as Router } from "react-router-dom";
import { Container, Row, Col, ButtonGroup, Button } from 'react-bootstrap';
import WebBleTransport from "@coolwallet/transport-web-ble";
import * as core from "@coolwallet/core";
import { getAppKeysOrGenerate } from "./utils/keypairUtil";
import Routes from "./components/Routes";
// import MyNavBar from "./components/NavBar";
import "./App.css";

const { appPublicKey, appPrivateKey } = getAppKeysOrGenerate();


class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = { transport: undefined, cardName: "", isLocked: false };
  }

  componentDidCatch(error, errorInfo) {
    console.debug("catched error :", error);
  }

  setIsLocked = (isLocked) => {
    this.setState({ isLocked });
  };

  connect = async () => {
    WebBleTransport.listen(async (error, device) => {
      console.log(device)
      if (device) {
        const cardName = device.name;
        const transport = await WebBleTransport.connect(device);
        const SEPublicKey = await core.config.getSEPublicKey(transport);
        this.setState({ transport, cardName, SEPublicKey });
        localStorage.setItem('cardName', cardName)
        localStorage.setItem('SEPublicKey', SEPublicKey)
        console.log(`SEPublicKey: ${SEPublicKey}`);
      } else {
        console.log(error);
      }
    });
  };

  disconnect = () => {
    WebBleTransport.disconnect(this.state.transport.device.id);
    this.setState({ transport: undefined, cardName: "" });
  };

  makeConnectComponent(btnName, onClick) {
    return (
      <Row className="function-component">
        <Col xs={2}>
          {'Card Name'}
        </Col>
        <Col className="show-text-area">
          {this.state.cardName}
        </Col>
        <Col xs={2}>
          <ButtonGroup className="d-flex">
            <Button variant='light' onClick={onClick}>{btnName}</Button>
          </ButtonGroup>
        </Col>
      </Row>
    )
  };

  render() {
    return (
      <div className="App">
        <Router>
          <Container>
            <Row className='title'>
              <p>CoolWallet Script Tester</p>
            </Row>
            {this.state.transport
              ? this.makeConnectComponent('Disconnect', this.disconnect)
              : this.makeConnectComponent('Connect', this.connect)
            }
          </Container>
          <Routes
            isLocked={this.state.isLocked}
            setIsLocked={this.setIsLocked}
            transport={this.state.transport}
            appPrivateKey={appPrivateKey}
            appPublicKey={appPublicKey}
          />
        </Router>
      </div>
    );
  }
}

export default App;

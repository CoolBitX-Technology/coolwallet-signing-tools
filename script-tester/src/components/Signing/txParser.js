import React from 'react';
import { Col, Row } from 'react-bootstrap';
import { ethers } from 'ethers';

const TxParser = ({ title, data, ...props }) => {
  const isComponentReady = data.length > 0;
  const parseTx = (rawData) => ethers.utils.parseTransaction(rawData);

  const renderTx = isComponentReady
    ? Object.entries(parseTx(data)).map(([key, value]) => (
        <div key={`${title}_${key}`} style={{ display: 'flex' }}>
          <p style={{ margin: 5 }}>
            <b>{key}: </b>
          </p>
          <p style={{ margin: 5, marginLeft: 10 }}>
            <span>{value?._isBigNumber ? value.toString() : value}</span>
          </p>
        </div>
      ))
    : null;

  return isComponentReady ? (
    <Row className='title2'>
      <p>{title}</p>
      <Col>{renderTx}</Col>
    </Row>
  ) : null;
};

export default TxParser;

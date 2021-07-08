import { Form, Row, Col, ButtonGroup, Button } from 'react-bootstrap';

export const makeGetComponent = (title, content, onClick, disabled) => (
  <Row className="function-component">
    <Col xs={2}>
      {title}
    </Col>
    <Col className="show-text-area">
      {content}
    </Col>
    <Col xs={2}>
      <ButtonGroup className="d-flex">
        <Button
          disabled={disabled}
          variant='outline-light'
          onClick={onClick}>
          {'Get'}
        </Button>
      </ButtonGroup>
    </Col>
  </Row>
);

export const makeSendComponent = (title, content, onClick, disabled, input, setInput, placeholder) => (
  <Row className="function-component">
    <Col xs={2}>
      {title}
    </Col>
    <Col xs={3} className="input-col">
      <Form.Control
        value={input}
        onChange={(event) => {
          setInput(event.target.value);
        }}
        placeholder={placeholder}
      />
    </Col>
    <Col className="show-text-area">
      {content}
    </Col>
    <Col xs={2}>
      <ButtonGroup className="d-flex">
        <Button
          disabled={disabled}
          variant='outline-light'
          onClick={onClick}>
          {'Send'}
        </Button>
      </ButtonGroup>
    </Col>
  </Row>
);

export const makeSendComponentWithTwoInput = (title, content, onClick, disabled, input, setInput, placeholder, input2, setInput2, placeholder2) => (
  <Row className="function-component">
    <Col xs={2}>
      {title}
    </Col>
    <Col xs={2} className="input-col">
      <Form.Control
        value={input}
        onChange={(event) => {
          setInput(event.target.value);
        }}
        placeholder={placeholder}
      />
    </Col>
    &ensp;
    <Col xs={2} className="input-col">
      <Form.Control
        value={input2}
        onChange={(event) => {
          setInput2(event.target.value);
        }}
        placeholder={placeholder2}
      />
    </Col>
    <Col xs={4} className="input-col">
      <Form.Control
        value={content}
        placeholder={'Final signature'}
        disabled
      />
    </Col>
    <Col xs={1}>
      <ButtonGroup className="d-flex">
        <Button
          disabled={disabled}
          variant='outline-light'
          onClick={onClick}>
          {'Send'}
        </Button>
      </ButtonGroup>
    </Col>
  </Row>
);

export const makeSendComponentWithThreeInput = (title, content, onClick, disabled, input, setInput, placeholder, input2, setInput2, placeholder2, input3, setInput3, placeholder3) => (
  <Row className="function-component">
    <Col xs={2}>
      {title}
    </Col>
    <Col xs={1} className="input-col">
      <Form.Control
        value={input}
        onChange={(event) => {
          setInput(event.target.value);
        }}
        placeholder={placeholder}
      />
    </Col>
    <Col xs={1} className="input-col">
      <Form.Control
        value={input2}
        onChange={(event) => {
          setInput2(event.target.value);
        }}
        placeholder={placeholder2}
      />
    </Col>
    <Col xs={2} className="input-col">
      <Form.Control
        value={input3}
        onChange={(event) => {
          setInput3(event.target.value);
        }}
        placeholder={placeholder3}
      />
    </Col>
    <Col xs={4} className="input-col">
      <Form.Control
        value={content}
        placeholder={'Final signature'}
        disabled
      />
    </Col>
    <Col xs={1}>
      <ButtonGroup className="d-flex">
        <Button
          disabled={disabled}
          variant='outline-light'
          onClick={onClick}>
          {'Send'}
        </Button>
      </ButtonGroup>
    </Col>
  </Row>
);

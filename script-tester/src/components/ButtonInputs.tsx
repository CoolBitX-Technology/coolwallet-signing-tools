import { ReactElement, memo } from 'react';
import clsx from 'clsx';
import { Row, Col, ButtonGroup, Button as BootStrapButton, Form } from 'react-bootstrap';

const row = clsx('text-lg', 'mt-4');

const textArea = clsx('flex', 'items-center', 'rounded', 'font-sans', 'bg-gray-500');

const inputCol = clsx('p-0', 'mr-4');

const buttonGroup = clsx('flex');

const button = clsx('disabled:bg-transparent', 'disabled:text-gray-400', 'disabled:border-gray-400');

interface Input {
  xs?: number;
  value: string;
  placeholder: string;
  onChange(value: string): void;
}

interface Props {
  title: string;
  inputs: Input[];
  content: string;
  onClick(): void;
  btnTitle?: string;
  variant?: string;
  disabled?: boolean;
}

const ButtonInputs = (props: Props): ReactElement => {
  return (
    <Row className={row}>
      <Col xs={2}>{props.title}</Col>
      {props.inputs.map((input, i) => (
        <Col xs={input.xs ?? 2} key={'' + i + ''} className={inputCol}>
          <Form.Control
            value={input.value}
            onChange={(event) => {
              input.onChange(event.target.value);
            }}
            placeholder={input.placeholder}
          />
        </Col>
      ))}
      <Col className={textArea}>{props.content}</Col>
      <Col xs={2}>
        <ButtonGroup className={buttonGroup}>
          <BootStrapButton className={button} variant={props.variant} disabled={props.disabled} onClick={props.onClick}>
            {props.btnTitle}
          </BootStrapButton>
        </ButtonGroup>
      </Col>
    </Row>
  );
};

ButtonInputs.defaultProps = { disabled: false, variant: 'light', btnTitle: 'Get' };

export default memo(ButtonInputs);

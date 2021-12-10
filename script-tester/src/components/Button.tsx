import { ReactElement, memo } from 'react';
import clsx from 'clsx';
import { Row, Col, ButtonGroup, Button as BootStrapButton } from 'react-bootstrap';

const row = clsx('text-lg', 'mt-4');

const textArea = clsx('flex', 'items-center', 'rounded', 'font-sans', 'bg-gray-500');

const buttonGroup = clsx('flex');

const button = clsx('disabled:bg-transparent', 'disabled:text-gray-400', 'disabled:border-gray-400');

interface Props {
  title: string;
  content: string;
  onClick(): void;
  btnTitle?: string;
  variant?: string;
  disabled?: boolean;
}

const Button = (props: Props): ReactElement => {
  return (
    <Row className={row}>
      <Col xs={2}>{props.title}</Col>
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

Button.defaultProps = { disabled: false, variant: 'light', btnTitle: 'Get' };

export default memo(Button);

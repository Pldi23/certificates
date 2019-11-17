import React, {useState} from "react";
import {Button, Modal, ModalFooter, ModalHeader} from "reactstrap";
import {getMessageByLocale} from "../app/Message";
import {MdShoppingCart} from "react-icons/md";

const pStyle = {
    marginLeft: '3px',
    marginRight: '3px',
};

const BuyButtonModal = (props) => {
    const {
        locale,
        certificate,
        onAddToBasket,
    } = props;

    const [modal, setModal] = useState(false);

    const toggle = () => setModal(!modal);

    const buyHandler = (certificate) => {
        console.log(certificate);
        onAddToBasket(certificate);
        toggle()
    };

    return (
        <div>
            <Button color={'danger'} style={pStyle} onClick={toggle}>
                <span><MdShoppingCart /></span>
                {/*{getMessageByLocale(locale, 'buy')}*/}
            </Button>
            <Modal isOpen={modal} toggle={toggle}>
                <ModalHeader toggle={toggle}>{getMessageByLocale(locale, 'addToCart')}</ModalHeader>
                <ModalFooter>
                    <Button color={'primary'} onClick={() => buyHandler(certificate)}>{getMessageByLocale(locale, 'toCart')}</Button>{' '}
                    <Button color="secondary" onClick={toggle}>{getMessageByLocale(locale, 'cancel')}</Button>{' '}
                </ModalFooter>
            </Modal>
        </div>
    );
};

export default BuyButtonModal;
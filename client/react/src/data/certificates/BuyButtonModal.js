import React, {useState} from "react";
import {Button, Modal, ModalFooter, ModalHeader} from "reactstrap";
import {getMessageByLocale} from "../../app/Message";

const BuyButtonModal = (props) => {
    const {
        locale,
        id,

    } = props;

    const [modal, setModal] = useState(false);

    const toggle = () => setModal(!modal);

    const buyHandler = (id) => {
        console.log(id);
        toggle()
    };

    return (
        <div>
            <Button color={'danger'} onClick={toggle}>{getMessageByLocale(locale, 'buy')}</Button>
            <Modal isOpen={modal} toggle={toggle}>
                <ModalHeader toggle={toggle}>{getMessageByLocale(locale, 'confirmPurchase')}</ModalHeader>
                <ModalFooter>
                    <Button color={'primary'} onClick={() => buyHandler(id)}>{getMessageByLocale(locale, 'confirmPurchase')}</Button>{' '}
                    <Button color="secondary" onClick={toggle}>{getMessageByLocale(locale, 'cancel')}</Button>{' '}
                </ModalFooter>
            </Modal>
        </div>
    );
};

export default BuyButtonModal;
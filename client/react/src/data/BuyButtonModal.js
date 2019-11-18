import React, {useState} from "react";
import {Button, Modal, ModalFooter, ModalHeader} from "reactstrap";
import {getMessageByLocale} from "../app/Message";
import {MdShoppingCart} from "react-icons/md";
import {withCookies} from "react-cookie";
import {COOKIES_VIEWED_CERTIFICATES} from "../constants";

const pStyle = {
    marginLeft: '3px',
    marginRight: '3px',
};

const BuyButtonModal = (props) => {
    const {
        locale,
        certificate,
        onAddToBasket,
        cookies
    } = props;

    const [modal, setModal] = useState(false);

    const toggle = () => setModal(!modal);

    const onClick = () => {
        let certificatesViewed = cookies.get(COOKIES_VIEWED_CERTIFICATES) ? cookies.get(COOKIES_VIEWED_CERTIFICATES) : 'name=';
        cookies.set(COOKIES_VIEWED_CERTIFICATES, certificatesViewed + ',' + certificate.name);
        toggle()
    };

    const buyHandler = (certificate) => {
        onAddToBasket(certificate);
        toggle()
    };

    return (
        <div>
            <Button color={'danger'} style={pStyle} onClick={onClick}>
                <span><MdShoppingCart /></span>
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

export default withCookies(BuyButtonModal);
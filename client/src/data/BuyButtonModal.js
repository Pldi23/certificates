import React, {useState} from "react";
import {Button, Modal, ModalFooter, ModalHeader} from "reactstrap";
import {getMessage, getMessageByLocale} from "../app/Message";
import {MdShoppingCart} from "react-icons/md";
import {withCookies} from "react-cookie";
import {COOKIES_VIEWED_CERTIFICATES} from "../constants";
import * as PropTypes from "prop-types";
import './Certificates.css'
import {getCertificateById} from "../service/APIService";
import Alert from "react-s-alert";

const BuyButtonModal = (props) => {

    BuyButtonModal.propTypes = {
        locale: PropTypes.string.isRequired,
        certificate: PropTypes.object.isRequired,
        onAddToBasket: PropTypes.func.isRequired,
        link: PropTypes.object.isRequired,
        reloadHandler: PropTypes.func.isRequired
    };

    const {
        locale,
        certificate,
        onAddToBasket,
        reloadHandler,
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
        getCertificateById(certificate.id, props).then(response => {
            if (!response.ok) {
                Alert.error(getMessage(props, 'certificateUnavailable'));
                reloadHandler();
            } else {
                onAddToBasket(certificate);
            }
        });
        toggle()
    };

    return (
        <div>
            <Button color={'danger'} className={'pStyle'} onClick={onClick}>
                <span><MdShoppingCart/></span>
            </Button>
            <Modal isOpen={modal} toggle={toggle}>
                <ModalHeader toggle={toggle}>{getMessageByLocale(locale, 'addToCart')}</ModalHeader>
                <ModalFooter>
                    <Button color={'primary'}
                            onClick={() => buyHandler(certificate)}>{getMessageByLocale(locale, 'toCart')}</Button>{' '}
                    <Button color="secondary" onClick={toggle}>{getMessageByLocale(locale, 'cancel')}</Button>{' '}
                </ModalFooter>
            </Modal>
        </div>
    );
};

export default withCookies(BuyButtonModal);
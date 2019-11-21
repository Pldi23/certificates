import React, {useState} from "react";
import {Button, Modal, ModalFooter, ModalHeader} from "reactstrap";
import {getMessage} from "../app/Message";
import {withCookies} from "react-cookie";
import {postOrder} from "../service/APIService";
import Alert from "react-s-alert";
import * as PropTypes from "prop-types";

const ConfirmPurchaseModal = (props) => {

    ConfirmPurchaseModal.propTypes = {
        basketCertificates: PropTypes.array.isRequired,
        onRefreshBasket: PropTypes.func.isRequired,
    };

    const {
        basketCertificates,
        onRefreshBasket
    } = props;

    const [modal, setModal] = useState(false);

    const toggle = () => setModal(!modal);

    const paymentHandler = (basketCertificates) => {
        const orderJson = certificatesToJson(basketCertificates);
        postOrder(orderJson, props)
            .then(response => {
                if (response.ok) {
                    response.json().then(json => {
                        Alert.success(getMessage(props, 'thanks'));
                    });
                } else {
                    response.json().then(json => {
                        let message = JSON.stringify(json.messages);
                        Alert.error(message.substring(1, message.length - 1));
                    });
                }
            }).catch(error => {
            Alert.error((error && error.message) || getMessage(props, 'error'));
        });
        onRefreshBasket();
    };

    const certificatesToJson = (basketCertificates) => {
        return JSON.stringify({
            giftCertificates: basketCertificates.map(certificate => {
                return {
                    id: certificate.id
                }
            })
        });
    };

    const sumPrice = (basketCertificates) => {
        let sum = 0;
        basketCertificates.forEach(item => {
            sum += item.price;
        });
        return sum;
    };

    return (
        <div>
            <Button color="primary" size="sm" onClick={toggle}>
                {getMessage(props, 'pay')}  {sumPrice(basketCertificates)}$</Button>
            <Modal isOpen={modal} toggle={toggle}>
                <ModalHeader toggle={toggle}>{getMessage(props, 'confirmPurchase')}</ModalHeader>
                <ModalFooter>
                    <Button color={'primary'} onClick={() => paymentHandler(basketCertificates)}>{getMessage(props, 'confirmPurchase')}</Button>{' '}
                    <Button color="secondary" onClick={toggle}>{getMessage(props, 'cancel')}</Button>{' '}
                </ModalFooter>
            </Modal>
        </div>
    );
};

export default withCookies(ConfirmPurchaseModal);
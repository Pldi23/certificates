import React, {useState} from "react";
import {Button, Modal, ModalFooter, ModalHeader} from "reactstrap";
import {getMessage} from "../app/Message";
import {withCookies} from "react-cookie";
import {postOrder} from "../service/APIService";
import Alert from "react-s-alert";
import * as PropTypes from "prop-types";
import {ACCESS_TOKEN} from "../constants";
import {NavLink} from "react-router-dom";

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
                } else if (response.status === 401) {
                    response.json().then(json => {
                        let message = JSON.stringify(json.messages);
                        Alert.error(message.substring(1, message.length - 1));
                    });
                    this.props.history.push('/login')
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
        return new Intl.NumberFormat('en', {style: 'currency', currency: 'USD'}).format(sum);
    };

    return (
        <div>
            <Button color="primary" size="sm" onClick={toggle}>
                {getMessage(props, 'pay')} {sumPrice(basketCertificates)}</Button>
            {localStorage.getItem(ACCESS_TOKEN) ? (
                <Modal isOpen={modal} toggle={toggle}>
                    <ModalHeader toggle={toggle}>{getMessage(props, 'confirmPurchase')}</ModalHeader>
                    < ModalFooter>
                        <Button color={'primary'}
                                 onClick={() => paymentHandler(basketCertificates)}>{getMessage(props, 'confirmPurchase')}</Button>{' '}
                        <Button color="secondary" onClick={toggle}>{getMessage(props, 'cancel')}</Button>{' '}
                    </ModalFooter>
                </Modal>

            ) : (
                <Modal isOpen={modal} toggle={toggle}>
                    <ModalHeader toggle={toggle}>{getMessage(props, 'notAuthorized')}</ModalHeader>
                    < ModalFooter>
                        <NavLink to="/login" className={'btn btn-primary text-center'}>{getMessage(props, 'login')}</NavLink>
                    </ModalFooter>
                </Modal>
            )

            }
        </div>
    );
};

export default withCookies(ConfirmPurchaseModal);
import React, {useState} from "react";
import {Button, Modal, ModalFooter, ModalHeader} from "reactstrap";
import {getMessageByLocale} from "../app/Message";
import {NavLink} from "react-router-dom";
import {MdShoppingCart} from "react-icons/md";
import * as PropTypes from "prop-types";

const BuyButtonNotAuthorizedModal = (props) => {

    BuyButtonNotAuthorizedModal.propTypes = {
        locale: PropTypes.string.isRequired,
    };

    const {
        locale
    } = props;

    const [modal, setModal] = useState(false);

    const toggle = () => setModal(!modal);

    return (
        <div>
            <Button color={'danger'} onClick={toggle}>
                <span><MdShoppingCart /></span>
            </Button>
            <Modal isOpen={modal} toggle={toggle}>
                <ModalHeader toggle={toggle}>{getMessageByLocale(locale, 'notAuthorized')}</ModalHeader>
                <ModalFooter>
                    <NavLink to="/login" className={'btn btn-primary text-center'}>{getMessageByLocale(locale, 'login')}</NavLink>{' '}
                    <Button color="secondary" onClick={toggle}>{getMessageByLocale(locale, 'later')}</Button>{' '}
                </ModalFooter>
            </Modal>
        </div>
    );
};

export default BuyButtonNotAuthorizedModal;
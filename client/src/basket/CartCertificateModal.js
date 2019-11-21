import React, {useState} from "react";
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from "reactstrap";
import {getMessage} from "../app/Message";
import {withCookies} from "react-cookie";
import * as PropTypes from "prop-types";
import './Basket.css'

const CartCertificateModal = (props) => {

    CartCertificateModal.propTypes = {
        certificate: PropTypes.object.isRequired,
    };

    const {
        certificate,
    } = props;

    const [modal, setModal] = useState(false);

    const toggle = () => setModal(!modal);

    return (
        <div>
            <button className={'titleStyle'} onClick={toggle}>{certificate.name}</button>
            <Modal isOpen={modal} toggle={toggle}>
                <ModalHeader toggle={toggle}>{certificate.name}</ModalHeader>
                <ModalBody>
                    <p className="lead">
                        {getMessage(props, 'descriptionModal')}:
                        {certificate.description}</p>
                    <hr className="my-2"/>
                    <p>
                        {getMessage(props, 'priceModal')}:
                        {certificate.price}$</p>
                    <p>
                        {getMessage(props, 'expiresModal')}:
                        {certificate.expirationDate}</p>
                    <p className="lead">
                    </p>
                </ModalBody>
                <ModalFooter>
                    <Button color="primary" onClick={toggle}>Ok</Button>{' '}
                </ModalFooter>
            </Modal>
        </div>
    );
};

export default withCookies(CartCertificateModal)
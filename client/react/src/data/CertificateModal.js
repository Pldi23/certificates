import {useState} from "react";
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from "reactstrap";
import React from "react";
import {getMessageByLocale} from "../app/Message";

const titleStyle = {
    fontWeight: 600,
    color: 'chocolate',
    backgroundColor: '#F7F7F7',
    borderTopWidth: '0px',
    borderLeftWidth: '0px',
    borderBottomWidth: '0px',
    borderRightWidth: '0px',
};

const CertificateModal = (props) => {
    const {
        certificate,
        tags,
        locale
    } = props;

    const [modal, setModal] = useState(false);

    const toggle = () => setModal(!modal);

    return (
        <div>
            <button style={titleStyle} onClick={toggle}>{certificate.name}</button>
            <Modal isOpen={modal} toggle={toggle}>
                <ModalHeader toggle={toggle}>{certificate.name}</ModalHeader>
                <ModalBody>
                    <p className="lead">{getMessageByLocale(locale, 'descriptionModal')}: {certificate.description}</p>
                    <hr className="my-2"/>
                    <p>{getMessageByLocale(locale, 'priceModal')}: {certificate.price}$</p>
                    <p>{getMessageByLocale(locale, 'expiresModal')}: {certificate.expirationDate}</p>
                    <p>{getMessageByLocale(locale, 'createdModal')}: {certificate.creationDate}</p>
                    <p>{tags}</p>
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

export default CertificateModal;
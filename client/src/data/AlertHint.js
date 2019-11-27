import {Card, CardBody, Collapse} from "reactstrap";
import {withCookies} from "react-cookie";
import {getMessage} from "../app/Message";
import React, {useState} from "react";
import './Certificates.css'

const AlertHint = (props) => {
    const [isOpen, setIsOpen] = useState(false);

    const toggle = () => setIsOpen(!isOpen);

    return (
        <div>
            <span>{getMessage(props, 'notReadableSearch')}</span>
            <hr></hr>
            <button className={'badge badge-info hintStyle'} onClick={toggle}>{getMessage(props, 'hint')}</button>
            <Collapse isOpen={isOpen}>
                <Card>
                    <CardBody>
                        <p className="text-primary">{props.hint === 'ordersSearch' ? getMessage(props, 'ordersSearchViolation') : getMessage(props, 'searchViolation')}</p>
                    </CardBody>
                </Card>
            </Collapse>
        </div>
    );
};

export default withCookies(AlertHint);
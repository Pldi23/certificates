import {Card, CardBody, Collapse} from "reactstrap";
import {withCookies} from "react-cookie";
import {getMessage} from "../app/Message";
import React, {useState} from "react";

const hintStyle = { marginBottom: '1rem' };

const AlertHint = (props) => {
    const [isOpen, setIsOpen] = useState(false);

    const toggle = () => setIsOpen(!isOpen);

    return (
        <div>
            <span>{getMessage(props, 'notReadableSearch')}</span>
            <hr></hr>
            <button className={'badge badge-info'} onClick={toggle} style={hintStyle}>{getMessage(props, 'hint')}</button>
            <Collapse isOpen={isOpen}>
                <Card>
                    <CardBody>
                        <p className="text-primary">{getMessage(props, 'searchViolation')}</p>
                    </CardBody>
                </Card>
            </Collapse>
        </div>
    );
};

export default withCookies(AlertHint);
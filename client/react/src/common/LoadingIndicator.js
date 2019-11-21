import React from 'react';
import {Spinner} from "reactstrap";

const indicatorStyle = {
    display: 'block',
    textAlign: 'center',
    marginTop: '30px'
};

const spinnerStyle = {
    width: '3rem',
    height: '3rem'
};

export default function LoadingIndicator(props) {
    return (
        <div className="loading-indicator" style={indicatorStyle}>
            <Spinner style={spinnerStyle} type="grow" />
        </div>
    );
}
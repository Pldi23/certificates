import React from 'react';
import {Spinner} from "reactstrap";

export default function LoadingIndicator(props) {
    return (
        <div className="loading-indicator" style={{display: 'block', textAlign: 'center', marginTop: '30px'}}>
            <Spinner style={{ width: '3rem', height: '3rem' }} type="grow" />
        </div>
    );
}
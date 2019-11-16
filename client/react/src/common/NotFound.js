import React, { Component } from 'react';
import './NotFound.css';
import {PREV_PATH} from "../constants";
import LocalizedStrings from 'react-localization';
import {message} from "../app/Message";

class NotFound extends Component {



    render() {
        const path = this.props.location;
        localStorage.setItem(PREV_PATH, path);
        let strings = new LocalizedStrings({data: message});
        strings.setContent(message);
        strings.setLanguage(this.props.locale);
        return (
            <div className="page-not-found">
                <h1 className="title">
                    404
                </h1>
                <div className="desc">
                    {strings.notFound}
                </div>
            </div>
        );
    }
}

export default NotFound;
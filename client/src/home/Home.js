import React, { Component } from 'react';
import './Home.css';
import LocalizedStrings from 'react-localization';
import {message} from "../app/Message";
import * as PropTypes from "prop-types";

class Home extends Component {

    static propTypes = {
        locale: PropTypes.string.isRequired
    };

    render() {
        let strings = new LocalizedStrings({data: message});
        strings.setContent(message);
        strings.setLanguage(this.props.locale);
        return (
            <div className="home-container">
                <div className="container">
                    <div className="graf-bg-container">
                        <div className="graf-layout">
                            <div className="graf-circle"></div>
                            <div className="graf-circle"></div>
                            <div className="graf-circle"></div>
                            <div className="graf-circle"></div>
                            <div className="graf-circle"></div>
                            <div className="graf-circle"></div>
                            <div className="graf-circle"></div>
                            <div className="graf-circle"></div>
                            <div className="graf-circle"></div>
                            <div className="graf-circle"></div>
                            <div className="graf-circle"></div>
                        </div>
                    </div>
                    <h1 className="home-title">{strings.slogan}</h1>
                </div>
            </div>
        )
    }
}

export default Home;
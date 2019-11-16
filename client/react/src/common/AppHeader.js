import React, {Component} from 'react';
import {NavLink} from 'react-router-dom';
import './AppHeader.css';
import LocalizedStrings from 'react-localization';
import {message} from '../app/Message'
import {CERTIFICATES_HREF, PREV_PATH} from "../constants";
import {Button} from "reactstrap";

class AppHeader extends Component {

    getPath() {
        return localStorage.getItem(PREV_PATH) ? localStorage.getItem(PREV_PATH) : '/'
    }

    render() {
        let strings = new LocalizedStrings({data: message});
        strings.setContent(message);
        strings.setLanguage(this.props.locale);
        return (
            <header className="app-header">
                <div className="container">
                    <div className="app-branding">
                        {this.props.currentUser && this.props.currentUser.user.role === 'ROLE_ADMIN' && this.props.currentRouteCertificates ?
                            <NavLink to="/add" className="app-title" onClick={() => {
                                localStorage.removeItem(CERTIFICATES_HREF);
                            }}>{strings.newCertificate}</NavLink> :
                            <NavLink to='/' className="app-title" onClick={() => {
                                localStorage.removeItem(CERTIFICATES_HREF);
                            }}>{strings.name}</NavLink>
                        }
                    </div>
                    <div className="app-options">
                        <nav className="app-nav">
                            {this.props.authenticated ? (
                                <ul>
                                    <li>
                                        <Button color="link" onClick={this.props.onLocale}>RU/EN</Button>
                                    </li>
                                    <li>
                                        <NavLink to="/certificates">{strings.certificates}</NavLink>
                                    </li>
                                    <li>
                                        <NavLink to="/profile" onClick={() => {
                                            localStorage.removeItem(CERTIFICATES_HREF);
                                        }}>{this.props.currentUser.user.email}</NavLink>
                                    </li>
                                    <li>
                                        <NavLink to="/login" onClick={this.props.onLogout}>{strings.logout}</NavLink>
                                    </li>
                                </ul>
                            ) : (
                                <ul>
                                    <li>
                                        <Button color="link" onClick={this.props.onLocale}>RU/EN</Button>
                                    </li>
                                    <li>
                                        <NavLink to="/certificates">{strings.certificates}</NavLink>
                                    </li>
                                    <li>
                                        <NavLink to="/login" onClick={() => {
                                            localStorage.removeItem(CERTIFICATES_HREF);
                                        }}>{strings.login}</NavLink>
                                    </li>
                                    <li>
                                        <NavLink to="/signup" onClick={() => {
                                            localStorage.removeItem(CERTIFICATES_HREF);
                                        }}>{strings.signup}</NavLink>
                                    </li>
                                </ul>
                            )}
                        </nav>
                    </div>
                </div>
            </header>
        )
    }
}

export default AppHeader;
import React, {Component} from 'react';
import {NavLink} from 'react-router-dom';
import './AppHeader.css';
import LocalizedStrings from 'react-localization';
import {message} from '../app/Message'
import {CERTIFICATES_HREF, ROLE_ADMIN, SEARCH_PARAMETERS} from "../constants";
import {Button} from "reactstrap";
import Basket from "../basket/Basket";


class AppHeader extends Component {


    render() {
        // console.log(this.props.certificatesUrl)
        let strings = new LocalizedStrings({data: message});
        strings.setContent(message);
        strings.setLanguage(this.props.locale);
        return (
            <header className="app-header">
                <div className="container">
                    <div className="app-branding">
                        {this.props.currentUser && this.props.currentUser.user.role === ROLE_ADMIN && this.props.currentRouteCertificates ?
                            <NavLink to="/add" className="app-title" onClick={() => {
                                localStorage.removeItem(CERTIFICATES_HREF);
                                localStorage.removeItem(SEARCH_PARAMETERS);

                            }}>{strings.newCertificate}</NavLink> :
                            <NavLink to='/' className="app-title" onClick={() => {
                                localStorage.removeItem(CERTIFICATES_HREF);
                                localStorage.removeItem(SEARCH_PARAMETERS);
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
                                        <NavLink
                                            to="/certificates"
                                            // to={{
                                            //     pathname: '/certificates',
                                            //     // search: this.props.certificatesUrlHandler,
                                            //     // hash: '#the-hash',
                                            //     // state: { fromDashboard: true }
                                            // }}
                                            // onClick={localStorage.setItem(CERTIFICATES_HREF, new URL(window.location.pathname))}

                                        >{strings.certificates}</NavLink>
                                    </li>
                                    <li>
                                        <NavLink to="/profile" onClick={() => {
                                            localStorage.removeItem(CERTIFICATES_HREF);
                                            localStorage.removeItem(SEARCH_PARAMETERS);
                                        }}>{this.props.currentUser.user.email}</NavLink>
                                    </li>
                                    <li>
                                        <NavLink to="/login" onClick={this.props.onLogout}>{strings.logout}</NavLink>
                                    </li>
                                    <li>
                                        <Basket
                                            basketCertificates={this.props.basketCertificates}
                                            onRemoveFromBasket={this.props.onRemoveFromBasket}
                                            onRefreshBasket={this.props.onRefreshBasket}
                                        />
                                    </li>
                                </ul>
                            ) : (
                                <ul>
                                    <li>
                                        <Button color="link" onClick={this.props.onLocale}>RU/EN</Button>
                                    </li>
                                    <li>
                                        <NavLink
                                            to="/certificates"
                                                 // to={{
                                                 //     pathname: '/certificates',
                                                 //     // search: this.props.certificatesUrlHandler,
                                                 //     // hash: '#the-hash',
                                                 //     // state: { fromDashboard: true }
                                                 // }}
                                                 // onClick={localStorage.setItem(CERTIFICATES_HREF, window.location.pathname)}
                                        >{strings.certificates}</NavLink>
                                    </li>
                                    <li>
                                        <NavLink to="/login" onClick={() => {
                                            localStorage.removeItem(CERTIFICATES_HREF);
                                            localStorage.removeItem(SEARCH_PARAMETERS);
                                        }}>{strings.login}</NavLink>
                                    </li>
                                    <li>
                                        <NavLink to="/signup" onClick={() => {
                                            localStorage.removeItem(CERTIFICATES_HREF);
                                            localStorage.removeItem(SEARCH_PARAMETERS);
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
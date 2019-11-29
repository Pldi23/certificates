import React, {Component} from 'react';
import './Login.css';
import {
    GOOGLE_AUTH_URL,
    FACEBOOK_AUTH_URL, ACCESS_TOKEN,
} from '../../constants';
import {Link, Redirect} from 'react-router-dom'
import fbLogo from '../../img/fb-logo.png';
import googleLogo from '../../img/google-logo.png';
import Alert from 'react-s-alert';
import {withCookies} from 'react-cookie';
import {getMessage, getMessageByLocale} from "../../app/Message";
import LoginForm from "./LoginForm";
import * as PropTypes from "prop-types";


class Login extends Component {

    static propTypes = {
        locale: PropTypes.string.isRequired
    };

    componentDidMount() {
        if (this.props.location.state && this.props.location.state.error) {
            setTimeout(() => {
                Alert.error(this.props.location.state.error, {
                    timeout: 5000
                });
                this.props.history.replace({
                    pathname: this.props.location.pathname,
                    state: {}
                });
            }, 100);
        }
    }

    render() {
        if (localStorage.getItem(ACCESS_TOKEN)) {
            return <Redirect
                to={{
                    pathname: "/",
                    state: {from: this.props.location}
                }}/>;
        }

        return (
            <div className="login-container">
                <div className="login-content">
                    <h1 className="login-title">{getMessage(this.props, 'loginLabel')}</h1>
                    <SocialLogin locale={this.props.cookies.cookies.locale}/>
                    <div className="or-separator">
                        <span className="or-text">{getMessage(this.props, 'or')}</span>
                    </div>
                    <LoginForm {...this.props} />
                    <span className="signup-link">{getMessage(this.props, 'newUser')}
                        <Link to="/signup">{getMessage(this.props, 'signUpLink')}</Link></span>
                </div>
            </div>
        );
    }
}

class SocialLogin extends Component {
    render() {
        return (
            <div className="social-login">
                <a className="btn btn-block social-btn google" href={GOOGLE_AUTH_URL}>
                    <img src={googleLogo} alt="Google"/>{getMessageByLocale(this.props.locale, 'loginGoogle')}</a>
                <a className="btn btn-block social-btn facebook" href={FACEBOOK_AUTH_URL}>
                    <img src={fbLogo} alt="Facebook"/>{getMessageByLocale(this.props.locale, 'loginFacebook')}</a>
            </div>
        );
    }
}




export default withCookies(Login)

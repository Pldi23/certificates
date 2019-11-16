import React, {Component} from 'react';
import './Login.css';
import {
    GOOGLE_AUTH_URL,
    FACEBOOK_AUTH_URL,
    ACCESS_TOKEN,
    ACCESS_TOKEN_EXPIRES_IN,
    REFRESH_TOKEN, PREV_PATH
} from '../../constants';
import {Link, Redirect} from 'react-router-dom'
import fbLogo from '../../img/fb-logo.png';
import googleLogo from '../../img/google-logo.png';
import Alert from 'react-s-alert';
import {withCookies} from 'react-cookie';
import {login} from "../../util/APIUtils";
import {getMessage, getMessageByLocale} from "../../app/Message";

class Login extends Component {

    componentDidMount() {
        // If the OAuth2 login encounters an error, the user is redirected to the /login page with an error.
        // Here we display the error and then remove the error query parameter from the location.
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
        if (this.props.authenticated) {
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


class LoginForm extends Component {


    constructor(props) {
        super(props);

        this.state = {
            email: '',
            password: '',
            message: '',
        };

        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount() {
        this.props.routeHandler(false);
    }

    handleInputChange(event) {
        const target = event.target;
        const inputName = target.name;
        const inputValue = target.value;

        this.setState({
            [inputName]: inputValue
        });
    }

    handleSubmit(event) {
        event.preventDefault();
        login(this.state.email, this.state.password, this.props)
            .then(response => {
                if (response.ok) {
                    localStorage.setItem(ACCESS_TOKEN, response.headers.get("Authorization"));
                    localStorage.setItem(REFRESH_TOKEN, response.headers.get("RefreshToken"));
                    localStorage.setItem(ACCESS_TOKEN_EXPIRES_IN, response.headers.get("ExpiresIn"));
                    localStorage.setItem(PREV_PATH, '/certificates');
                    this.props.loginHandler();
                    // Alert.success(getMessage(this.props, 'loginSuccess'));
                    this.props.history.push("/certificates");

                } else {
                    response.json().then(json => {
                        let message = JSON.stringify(json.messages);
                        Alert.error(message.substring(1, message.length - 1))
                    });
                    localStorage.setItem(PREV_PATH, '/login');
                    this.props.history.push("/login");
                }
            })
            .catch(error => {
                Alert.error(
                    (error && error.message) ||
                    getMessage(this.props, 'error'));
            });
    }

    render() {

        return (
            <div className="form-item">
                <form onSubmit={this.handleSubmit}>
                    <div className="form-item">
                        <input type="email" name="email"
                               className="form-control" placeholder={getMessage(this.props, 'emailPlaceholder')}
                               value={this.state.email} onChange={this.handleInputChange} required/>
                    </div>
                    <div className="form-item">
                        <input type="password" name="password"
                               className="form-control" placeholder={getMessage(this.props, 'passwordPlaceholder')}
                               value={this.state.password} onChange={this.handleInputChange} required/>
                    </div>
                    <div className="form-item">
                        <button type="submit" className="btn btn-block btn-primary">{getMessage(this.props, 'login')}</button>
                    </div>
                </form>
                <Link to="/certificates" className="btn btn-block btn-primary">{getMessage(this.props, 'cancel')}</Link>
            </div>
        );
    }
}

export default withCookies(Login)

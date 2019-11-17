import React, {Component} from 'react';
import './Login.css';
import {
    GOOGLE_AUTH_URL,
    FACEBOOK_AUTH_URL,
    ACCESS_TOKEN,
    ACCESS_TOKEN_EXPIRES_IN,
    REFRESH_TOKEN, PREV_PATH, EMAIL_REGEX_PATTERN, PASSWORD_REGEX_PATTERN
} from '../../constants';
import {Link, Redirect} from 'react-router-dom'
import fbLogo from '../../img/fb-logo.png';
import googleLogo from '../../img/google-logo.png';
import Alert from 'react-s-alert';
import {withCookies} from 'react-cookie';
import {login} from "../../util/APIUtils";
import {getMessage, getMessageByLocale} from "../../app/Message";
import {AvFeedback, AvForm, AvGroup, AvInput} from 'availity-reactstrap-validation';


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
            email: {
                value: '',
                isValid: false
            },
            password: {
                value: '',
                isValid: false
            }
        };
    }

    componentDidMount() {
        this.props.routeHandler(false);
    }

    handleInputEmail = (event) => {
        if (event.target.value.match(EMAIL_REGEX_PATTERN) && event.target.value.length > 0) {
            this.setState({
                email: {
                    value: event.target.value,
                    isValid: true
                }
            })
        } else {
            this.setState({
                email: {
                    value: event.target.value,
                    isValid: false
                }
            })
        }
    };

    handleInputPassword = (event) => {
        if (event.target.value.match(PASSWORD_REGEX_PATTERN) &&
            event.target.value.length > 0) {
            this.setState({
                password: {
                    value: event.target.value,
                    isValid: true
                }
            })
        } else {
            this.setState({
                password: {
                    value: event.target.value,
                    isValid: false
                }
            })
        }
    };

    handleSubmit = () => {
        login(this.state.email.value, this.state.password.value, this.props)
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
    };

    render() {

        const isFormValid = this.state.email.isValid && this.state.password.isValid;

        return (
            <div className="form-item">
                <AvForm onSubmit={this.handleSubmit}>
                    <AvGroup className="form-item">
                        <AvInput type="text"
                                 name="email"
                                 className="form-control"
                                 placeholder={getMessage(this.props, 'emailPlaceholder')}
                                 value={this.state.email.value}
                                 onChange={this.handleInputEmail}
                                 pattern={EMAIL_REGEX_PATTERN}

                        />
                        <AvFeedback>
                            {getMessage(this.props, 'emailViolation')}
                        </AvFeedback>
                    </AvGroup>
                    <AvGroup className="form-item">
                        <AvInput type="password"
                                 name="password"
                                 className="form-control"
                                 placeholder={getMessage(this.props, 'passwordPlaceholder')}
                                 value={this.state.password.value}
                                 onChange={this.handleInputPassword}
                                 pattern={PASSWORD_REGEX_PATTERN}/>
                        <AvFeedback>
                            {getMessage(this.props, 'passwordViolation')}
                        </AvFeedback>
                    </AvGroup>
                    <div className="form-item">
                        <button type="submit"
                                disabled={!isFormValid}
                                className="btn btn-block btn-primary">
                            {getMessage(this.props, 'login')}
                        </button>
                    </div>
                </AvForm>
                <Link to="/certificates" className="btn btn-block btn-primary">{getMessage(this.props, 'cancel')}</Link>
            </div>
        );
    }
}

export default withCookies(Login)

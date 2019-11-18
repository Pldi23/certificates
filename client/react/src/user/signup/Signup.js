import React, {Component} from 'react';
import './Signup.css';
import {Link, Redirect} from 'react-router-dom'
import {
    GOOGLE_AUTH_URL,
    FACEBOOK_AUTH_URL,
    ACCESS_TOKEN,
    REFRESH_TOKEN,
    ACCESS_TOKEN_EXPIRES_IN,
    PREV_PATH,
    EMAIL_REGEX_PATTERN,
    PASSWORD_REGEX_PATTERN,
    ROLE_USER,
    AUTHORIZATION_HEADER,
    REFRESH_HEADER, EXPIRES_IN_HEADER
} from '../../constants';
import {signup} from '../../service/APIService';
import fbLogo from '../../img/fb-logo.png';
import googleLogo from '../../img/google-logo.png';
import Alert from 'react-s-alert';
import {withCookies} from 'react-cookie';
import {getMessage, getMessageByLocale} from "../../app/Message";
import {AvFeedback, AvForm, AvGroup, AvInput} from 'availity-reactstrap-validation';


class Signup extends Component {
    render() {
        if (this.props.authenticated) {
            return <Redirect
                to={{
                    pathname: "/",
                    state: {from: this.props.location}
                }}/>;
        }

        return (
            <div className="signup-container">
                <div className="signup-content">
                    <h1 className="signup-title">{getMessage(this.props, 'signupLabel')}</h1>
                    <SocialSignup locale={this.props.cookies.cookies.locale}/>
                    <div className="or-separator">
                        <span className="or-text">{getMessage(this.props, 'or')}</span>
                    </div>
                    <SignupForm {...this.props} />
                    <span className="login-link">{getMessage(this.props, 'haveAccount')}
                        <Link to="/login">{getMessage(this.props, 'loginLink')}</Link></span>
                </div>
            </div>
        );
    }
}


class SocialSignup extends Component {
    render() {
        return (
            <div className="social-signup">
                <a className="btn btn-block social-btn google" href={GOOGLE_AUTH_URL}>
                    <img src={googleLogo} alt="Google"/>{getMessageByLocale(this.props.locale, 'signUpGoogle')}</a>
                <a className="btn btn-block social-btn facebook" href={FACEBOOK_AUTH_URL}>
                    <img src={fbLogo} alt="Facebook"/>{getMessageByLocale(this.props.locale, 'signUpFacebook')}</a>
            </div>
        );
    }
}



class SignupForm extends Component {


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

        const userJson = JSON.stringify({
            email: this.state.email.value,
            password: this.state.password.value,
            role: ROLE_USER
        });

        signup(userJson, this.props)
            .then(response => {
                if (response.ok) {
                    localStorage.setItem(ACCESS_TOKEN, response.headers.get(AUTHORIZATION_HEADER));
                    localStorage.setItem(REFRESH_TOKEN, response.headers.get(REFRESH_HEADER));
                    localStorage.setItem(ACCESS_TOKEN_EXPIRES_IN, response.headers.get(EXPIRES_IN_HEADER));
                    response.json().then(json => {
                        this.props.signUpHandler(json);
                        Alert.success(getMessage(this.props, 'signUpSuccess'));
                    });
                    localStorage.setItem(PREV_PATH, '/certificates');
                    this.props.history.push("/certificates");
                } else {
                    response.json().then(json => {
                        let message = JSON.stringify(json.messages);
                        Alert.error(message.substring(1, message.length - 1));
                    });
                    localStorage.setItem(PREV_PATH, '/signup');
                    this.props.history.push("/signup");
                }
            }).catch(error => {
            Alert.error((error && error.message) || getMessage(this.props, 'error'));
        });
    };

    render() {

        const email = this.state.email;
        const password = this.state.password;
        const isFormValid = email.isValid && password.isValid;


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
                               pattern={PASSWORD_REGEX_PATTERN}
                        />
                        <AvFeedback>
                            {getMessage(this.props, 'passwordViolation')}
                        </AvFeedback>
                    </AvGroup>
                    <div className="form-item">
                        <button type="submit" disabled={!isFormValid}
                                className="btn btn-block btn-primary">{getMessage(this.props, 'signup')}</button>
                    </div>
                </AvForm>
                <Link to="/certificates" className="btn btn-block btn-primary">{getMessage(this.props, 'cancel')}</Link>
            </div>

        );
    }
}

export default withCookies(Signup)
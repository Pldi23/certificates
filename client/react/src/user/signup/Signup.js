import React, {Component} from 'react';
import './Signup.css';
import {Link, Redirect} from 'react-router-dom'
import {
    GOOGLE_AUTH_URL,
    FACEBOOK_AUTH_URL,
    ACCESS_TOKEN,
    REFRESH_TOKEN,
    ACCESS_TOKEN_EXPIRES_IN, PREV_PATH
} from '../../constants';
import {signup} from '../../util/APIUtils';
import fbLogo from '../../img/fb-logo.png';
import googleLogo from '../../img/google-logo.png';
import Alert from 'react-s-alert';
import {withCookies} from 'react-cookie';
import {getMessage, getMessageByLocale} from "../../app/Message";

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
            email: '',
            password: ''
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

        const userJson = JSON.stringify({
            email: this.state.email,
            password: this.state.password,
            role: 'ROLE_USER'
        });

        signup(userJson, this.props)
            .then(response => {
                if (response.ok) {
                    localStorage.setItem(ACCESS_TOKEN, response.headers.get("Authorization"));
                    localStorage.setItem(REFRESH_TOKEN, response.headers.get("RefreshToken"));
                    localStorage.setItem(ACCESS_TOKEN_EXPIRES_IN, response.headers.get("ExpiresIn"));
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
                        <button type="submit" className="btn btn-block btn-primary">{getMessage(this.props, 'signup')}</button>
                    </div>
                </form>
                <Link to="/certificates" className="btn btn-block btn-primary">{getMessage(this.props, 'cancel')}</Link>
            </div>

        );
    }
}

export default withCookies(Signup)
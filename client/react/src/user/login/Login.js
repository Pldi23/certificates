import React, {Component} from 'react';
import './Login.css';
import {
    GOOGLE_AUTH_URL,
    FACEBOOK_AUTH_URL,
    ACCESS_TOKEN,
    ACCESS_TOKEN_EXPIRES_IN,
    REFRESH_TOKEN
} from '../../constants';
import {Link, Redirect} from 'react-router-dom'
import fbLogo from '../../img/fb-logo.png';
import googleLogo from '../../img/google-logo.png';
import Alert from 'react-s-alert';
import {withCookies} from 'react-cookie';
import {login} from "../../util/APIUtils";

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
                    pathname: "/not found path it was just / but could be /certificates",
                    state: {from: this.props.location}
                }}/>;
        }

        return (
            <div className="login-container">
                <div className="login-content">
                    <h1 className="login-title">Login to Gift Certificates</h1>
                    <SocialLogin/>
                    <div className="or-separator">
                        <span className="or-text">OR</span>
                    </div>
                    <LoginForm {...this.props} />
                    <span className="signup-link">New user? <Link to="/signup">Sign up!</Link></span>
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
                    <img src={googleLogo} alt="Google"/> Log in with Google</a>
                <a className="btn btn-block social-btn facebook" href={FACEBOOK_AUTH_URL}>
                    <img src={fbLogo} alt="Facebook"/> Log in with Facebook</a>
            </div>
        );
    }
}


class LoginForm extends Component {

    state = {
        email: '',
        password: '',
        message: '',
    };

    constructor(props) {
        super(props);

        const {cookies} = props;
        this.state.csrfToken = cookies.get('XSRF-TOKEN');
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
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
        login(this.state.email, this.state.password, this.state.csrfToken, this.props)
        // let url = new URL(API_BASE_URL + "/authenticate?"),
        //     params = {username: this.state.email, password: this.state.password};
        // Object.keys(params).forEach(key => url.searchParams.append(key, params[key]));
        // fetch(url, {
        //     method: 'POST',
        //     credentials: 'include',
        //     headers: {
        //         'X-XSRF-TOKEN': this.state.csrfToken,
        //         'Accept': 'application/json',
        //         'Content-Type': 'application/json',
        //     },
        // })
            .then(response => {
                if (response.ok) {
                    localStorage.setItem(ACCESS_TOKEN, response.headers.get("Authorization"));
                    localStorage.setItem(REFRESH_TOKEN, response.headers.get("RefreshToken"));
                    localStorage.setItem(ACCESS_TOKEN_EXPIRES_IN, response.headers.get("ExpiresIn"));
                    this.props.loginHandler();
                    Alert.success("You're successfully logged in! ");
                    this.props.history.push("/certificates");

                } else {
                    response.json().then(json => {
                        let message = JSON.stringify(json.messages);
                        Alert.error(message.substring(1, message.length - 1))
                    });
                    this.props.history.push("/login");
                }
            })
            .catch(error => {
                Alert.error(
                    (error && error.message) ||
                    'Oops! Something went wrong. Please try again!');
            });
    }

    render() {

        return (
            <div className="form-item">
                <form onSubmit={this.handleSubmit}>
                    <div className="form-item">
                        <input type="email" name="email"
                               className="form-control" placeholder="Enter your email"
                               value={this.state.email} onChange={this.handleInputChange} required/>
                    </div>
                    <div className="form-item">
                        <input type="password" name="password"
                               className="form-control" placeholder="Enter password"
                               value={this.state.password} onChange={this.handleInputChange} required/>
                    </div>
                    <div className="form-item">
                        <button type="submit" className="btn btn-block btn-primary">Login</button>
                    </div>
                </form>
                <Link to="/certificates" className="btn btn-block btn-primary">Cancel</Link>
            </div>
        );
    }
}

export default withCookies(Login)

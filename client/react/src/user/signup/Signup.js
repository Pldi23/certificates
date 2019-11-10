import React, {Component} from 'react';
import './Signup.css';
import {Link, Redirect} from 'react-router-dom'
import {
    GOOGLE_AUTH_URL,
    FACEBOOK_AUTH_URL,
    ACCESS_TOKEN,
    REFRESH_TOKEN,
    ACCESS_TOKEN_EXPIRES_IN
} from '../../constants';
import {signup} from '../../util/APIUtils';
import fbLogo from '../../img/fb-logo.png';
import googleLogo from '../../img/google-logo.png';
import Alert from 'react-s-alert';
import {withCookies} from 'react-cookie';

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
                    <h1 className="signup-title">Signup with SpringSocial</h1>
                    <SocialSignup/>
                    <div className="or-separator">
                        <span className="or-text">OR</span>
                    </div>
                    <SignupForm {...this.props} />
                    <span className="login-link">Already have an account? <Link to="/login">Login!</Link></span>
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
                    <img src={googleLogo} alt="Google"/> Sign up with Google</a>
                <a className="btn btn-block social-btn facebook" href={FACEBOOK_AUTH_URL}>
                    <img src={fbLogo} alt="Facebook"/> Sign up with Facebook</a>
                {/*<a className="btn btn-block social-btn github" href={GITHUB_AUTH_URL}>*/}
                {/*    <img src={githubLogo} alt="Github" /> Sign up with Github</a>*/}
            </div>
        );
    }
}

class SignupForm extends Component {

    state = {
        email: '',
        password: ''
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

        const userJson = JSON.stringify({
            email: this.state.email,
            password: this.state.password,
            role: 'ROLE_USER'
        });

        signup(userJson, this.state.csrfToken)
            .then(response => {
                if (response.ok) {
                    localStorage.setItem(ACCESS_TOKEN, response.headers.get("Authorization"));
                    localStorage.setItem(REFRESH_TOKEN, response.headers.get("RefreshToken"));
                    localStorage.setItem(ACCESS_TOKEN_EXPIRES_IN, response.headers.get("ExpiresIn"));
                    response.json().then(json => {
                        this.props.signUpHandler(json);
                        Alert.success("You're successfully registered!");
                    });
                    this.props.history.push("/certificates");
                } else {
                    response.json().then(json => {
                        let message = JSON.stringify(json.messages);
                        Alert.error(message.substring(1, message.length - 1));
                    });
                    this.props.history.push("/signup");
                }
            }).catch(error => {
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
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
                        <button type="submit" className="btn btn-block btn-primary">Sign up</button>
                    </div>
                </form>
                <Link to="/certificates" className="btn btn-block btn-primary">Cancel</Link>
            </div>

        );
    }
}

export default withCookies(Signup)
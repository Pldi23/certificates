import React, {Component} from "react";
import {
    ACCESS_TOKEN,
    ACCESS_TOKEN_EXPIRES_IN, AUTHORIZATION_HEADER,
    EMAIL_REGEX_PATTERN, EXPIRES_IN_HEADER,
    PASSWORD_REGEX_PATTERN, PREV_PATH, REFRESH_HEADER,
    REFRESH_TOKEN
} from "../../constants";
import {login} from "../../service/APIService";
import Alert from "react-s-alert";
import {getMessage, getMessageByLocale} from "../../app/Message";
import {AvFeedback, AvForm, AvGroup, AvInput} from "availity-reactstrap-validation";
import {Link} from "react-router-dom";

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
                    localStorage.setItem(ACCESS_TOKEN, response.headers.get(AUTHORIZATION_HEADER));
                    localStorage.setItem(REFRESH_TOKEN, response.headers.get(REFRESH_HEADER));
                    localStorage.setItem(ACCESS_TOKEN_EXPIRES_IN, response.headers.get(EXPIRES_IN_HEADER));
                    localStorage.setItem(PREV_PATH, '/certificates');
                    this.props.loginHandler();
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

        const locale = this.props.locale;

        return (
            <div className="form-item">
                <AvForm onSubmit={this.handleSubmit}>
                    <AvGroup className="form-item">
                        <AvInput type="text"
                                 name="email"
                                 className="form-control"
                                 placeholder={getMessageByLocale(locale, 'emailPlaceholder')}
                                 value={this.state.email.value}
                                 onChange={this.handleInputEmail}
                                 pattern={EMAIL_REGEX_PATTERN}

                        />
                        <AvFeedback>
                            {getMessageByLocale(locale, 'emailViolation')}
                        </AvFeedback>
                    </AvGroup>
                    <AvGroup className="form-item">
                        <AvInput type="password"
                                 name="password"
                                 className="form-control"
                                 placeholder={getMessageByLocale(locale, 'passwordPlaceholder')}
                                 value={this.state.password.value}
                                 onChange={this.handleInputPassword}
                                 pattern={PASSWORD_REGEX_PATTERN}/>
                        <AvFeedback>
                            {getMessageByLocale(locale, 'passwordViolation')}
                        </AvFeedback>
                    </AvGroup>
                    <div className="form-item">
                        <button type="submit"
                                disabled={!isFormValid}
                                className="btn btn-block btn-primary">
                            {getMessageByLocale(locale, 'login')}
                        </button>
                    </div>
                </AvForm>
                <Link to="/certificates" className="btn btn-block btn-primary">
                    {getMessageByLocale(locale, 'cancel')}
                </Link>
            </div>
        );
    }
}

export default LoginForm;
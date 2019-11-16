import React, {Component} from 'react';
import {Route, Switch} from 'react-router-dom';
import AppHeader from '../common/AppHeader';
import AppFooter from '../common/AppFooter';
import Home from '../home/Home';
import Login from '../user/login/Login';
import Certificates from '../data/certificates/Certificates';
import Signup from '../user/signup/Signup';
import Profile from '../user/profile/Profile';
import OAuth2RedirectHandler from '../user/oauth2/OAuth2RedirectHandler';
import NotFound from '../common/NotFound';
import LoadingIndicator from '../common/LoadingIndicator';
import {getCurrentUser} from '../util/APIUtils';
import {ACCESS_TOKEN, ACCESS_TOKEN_EXPIRES_IN, CERTIFICATES_HREF, PREV_PATH, REFRESH_TOKEN} from '../constants';
import PrivateRoute from '../common/PrivateRoute';
import Alert from 'react-s-alert';
import 'react-s-alert/dist/s-alert-default.css';
import 'react-s-alert/dist/s-alert-css-effects/slide.css';
import './App.css';
import {CookiesProvider, withCookies} from 'react-cookie';
import CreateCertificate from "../data/certificates/EditCertificate";
import LocalizedStrings from 'react-localization';
import {message} from './Message'
import DeleteLink from "../data/certificates/DeleteLink";


class App extends Component {


    constructor(props) {
        super(props);
        const {cookies} = props;
        this.state = {
            authenticated: false,
            currentUser: null,
            loading: false,
            currentRouteCertificates: false,
            locale: cookies.get('locale') ? cookies.get('locale') : 'en'
        };

        this.loadCurrentlyLoggedInUser = this.loadCurrentlyLoggedInUser.bind(this);
    }

    componentWillReceiveProps(nextProps){
        if (this.props.cookies.cookies.locale !== nextProps.cookies.cookies.locale)
            this.setState({locale: nextProps.cookies.cookies.locale})
    }


    loadCurrentlyLoggedInUser() {
        this.setState({
            loading: true
        });
        getCurrentUser(this.props)
            .then(response => {
                this.setState({
                    currentUser: response,
                    authenticated: true,
                    loading: false
                });
            }).catch(error => {
            this.setState({
                loading: false
            });
            console.log(error)
        });
    }

    handleLocale = () => {
        const {cookies} = this.props;
        if (this.state.locale === 'en') {

            cookies.set('locale', 'ru', {path: '/'});
            this.setState({locale: 'ru'});
        }
        if (this.state.locale === 'ru') {
            cookies.set('locale', 'en', {path: '/'});
            this.setState({locale: 'en'});
        }
    };


    handleLogout = () => {
        localStorage.removeItem(ACCESS_TOKEN);
        localStorage.removeItem(ACCESS_TOKEN_EXPIRES_IN);
        localStorage.removeItem(REFRESH_TOKEN);
        localStorage.removeItem(CERTIFICATES_HREF);
        localStorage.setItem(PREV_PATH, '/login');
        this.setState({
            authenticated: false,
            currentUser: null
        });
        let strings = new LocalizedStrings({data: message});
        strings.setContent(message);
        strings.setLanguage(this.state.locale);
        Alert.info(strings.successfullLogout);
    };

    loginHandler = () => {
        this.loadCurrentlyLoggedInUser();
    };

    routeHandler = (value) => {
        this.setState({
            currentRouteCertificates: value
        });

    };

    signUpHandler = (currentUser) => {
        this.setState({
            currentUser: currentUser,
            authenticated: true,
            loading: false
        })
    };

    componentDidMount() {
        this.loadCurrentlyLoggedInUser();
    }

    render() {


        if (this.state.loading) {
            return <LoadingIndicator/>
        }

        return (
            <CookiesProvider>
                <div className="app">
                    <div className="fixed-top">
                        <AppHeader authenticated={this.state.authenticated}
                                   currentUser={this.state.currentUser}
                                   currentRouteCertificates={this.state.currentRouteCertificates}
                                   onLogout={this.handleLogout}
                                   onLocale={this.handleLocale}
                                   locale={this.state.locale}/>
                    </div>
                    <div className="app-body">
                        <Switch>
                            <Route exact path='/'
                                render={(props) => <Home
                                    locale={this.state.locale}
                                    {...props}
                                />}
                            />
                            <PrivateRoute path="/profile" authenticated={this.state.authenticated}
                                          currentUser={this.state.currentUser}
                                          routeHandler={this.routeHandler}
                                          locale={this.state.locale}
                                          component={Profile}/>
                            <Route path="/certificates"
                                   render={(props) => <Certificates loginHandler={this.loginHandler}
                                                                    routeHandler={this.routeHandler}
                                                                    authenticated={this.state.authenticated}
                                                                    {...props} />}/>
                            <Route path="/login"
                                   render={(props) => <Login authenticated={this.state.authenticated}
                                                             loginHandler={this.loginHandler}
                                                             routeHandler={this.routeHandler}
                                                             {...props} />}/>
                            <Route path="/signup"
                                   render={(props) => <Signup
                                       authenticated={this.state.authenticated}
                                       signUpHandler={this.signUpHandler}
                                       routeHandler={this.routeHandler}
                                       {...props} />}/>
                            <Route path="/add"
                                   render={(props) => <CreateCertificate
                                       routeHandler={this.routeHandler}
                                       {...props} />}
                            />
                            <Route path="/edit"
                                   render={(props) => <CreateCertificate
                                       routeHandler={this.routeHandler}
                                       {...props} />}
                            />
                            <Route path="/oauth2/redirect" component={OAuth2RedirectHandler}/>
                            <Route path="/delete" component={DeleteLink}/>
                            <Route render={(props) => <NotFound
                                locale={this.state.locale}{...props}/>}/>
                        </Switch>
                    </div>
                    <AppFooter
                        locale={this.state.locale}
                    />
                    <Alert stack={{limit: 5}}
                           timeout={5000}
                           position='top-right' effect='slide' offset={65}/>
                </div>
            </CookiesProvider>
        );
    }
}

export default withCookies(App);

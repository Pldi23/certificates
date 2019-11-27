import React, {Component} from 'react';
import {Route, Switch} from 'react-router-dom';
import AppHeader from '../common/AppHeader';
import AppFooter from '../common/AppFooter';
import Home from '../home/Home';
import Login from '../user/login/Login';
import Certificates from '../data/Certificates';
import Signup from '../user/signup/Signup';
import Profile from '../user/profile/Profile';
import OAuth2RedirectHandler from '../user/oauth2/OAuth2RedirectHandler';
import NotFound from '../common/NotFound';
import LoadingIndicator from '../common/LoadingIndicator';
import {getCertificateById, getCurrentUser} from '../service/APIService';
import {
    ACCESS_TOKEN,
    ACCESS_TOKEN_EXPIRES_IN, APP_DEFAULT_LOCALE,
    CERTIFICATES_HREF, COOKIES_CART,
    COOKIES_LOCALE,
    REFRESH_TOKEN, SEARCH_PARAMETERS
} from '../constants';
import PrivateRoute from '../common/PrivateRoute';
import Alert from 'react-s-alert';
import 'react-s-alert/dist/s-alert-default.css';
import 'react-s-alert/dist/s-alert-css-effects/slide.css';
import './App.css';
import {CookiesProvider, withCookies} from 'react-cookie';
import EditCertificate from "../data/EditCertificate";
import LocalizedStrings from 'react-localization';
import {getMessage, getMessageByLocale, message} from './Message'
import DeleteLink from "../data/DeleteLink";
import {BrowserRouter as Router} from 'react-router-dom';


class App extends Component {

    constructor(props) {
        super(props);
        const {cookies} = props;
        this.state = {
            authenticated: false,
            currentUser: null,
            loading: false,
            currentRouteCertificates: false,
            locale: cookies.get(COOKIES_LOCALE) ? cookies.get(COOKIES_LOCALE) : APP_DEFAULT_LOCALE,
            basketCertificates: [],
        };
    }

    loadCurrentlyLoggedInUser = () => {
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
                loading: false,
                authenticated: false,
            });

        });
    };

    handleLocale = () => {
        const {cookies} = this.props;
        if (this.state.locale === 'en') {

            cookies.set(COOKIES_LOCALE, 'ru', {path: '/'});
            this.setState({locale: 'ru'});
        }
        if (this.state.locale === 'ru') {
            cookies.set(COOKIES_LOCALE, 'en', {path: '/'});
            this.setState({locale: 'en'});
        }
    };


    handleLogout = () => {
        localStorage.removeItem(ACCESS_TOKEN);
        localStorage.removeItem(ACCESS_TOKEN_EXPIRES_IN);
        localStorage.removeItem(REFRESH_TOKEN);
        localStorage.removeItem(CERTIFICATES_HREF);
        localStorage.removeItem(SEARCH_PARAMETERS);
        const {cookies} = this.props;
        cookies.remove(COOKIES_CART);
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
        if (!this.state.currentUser) {
            this.loadCurrentlyLoggedInUser();
        }
    };

    routeHandler = (value) => {
        this.setState({
            currentRouteCertificates: value
        });

    };

    signUpHandler = (currentUser) => {
        if (!this.state.currentUser) {
            this.setState({
                currentUser: currentUser,
                authenticated: true,
                loading: false
            })
        }
    };

    onAddToBasket = (newCertificate) => {
        if (this.state.basketCertificates.length < 10) {
            this.setState(prevState => ({
                basketCertificates: [...prevState.basketCertificates, newCertificate]
            }));
            const {cookies} = this.props;
            cookies.get(COOKIES_CART) ?
                cookies.set(COOKIES_CART, cookies.get(COOKIES_CART) + ',' + newCertificate.id) :
                cookies.set(COOKIES_CART, newCertificate.id, {path: '/'});
        } else {
            Alert.info(getMessageByLocale(this.state.locale, 'fullCart'))
        }
    };

    onRemoveFromBasket = (oldCertificate) => {

        let array = [...this.state.basketCertificates];
        let index = array.indexOf(oldCertificate);
        if (index !== -1) {
            array.splice(index, 1);
            this.setState({basketCertificates: array});
        }
        const {cookies} = this.props;
        let cookiesString = '';
        array.forEach(cert => cookiesString = cookiesString.concat(cert.id + ','));
        cookiesString = cookiesString.replace(/,$/, "");
        cookies.set(COOKIES_CART, cookiesString)

    };

    onRefreshBasket = () => {
        const {cookies} = this.props;
        cookies.remove(COOKIES_CART);
        this.setState({
            basketCertificates: []
        })
    };

    componentDidMount() {
        this.loadCurrentlyLoggedInUser();

        const {cookies} = this.props;
        const certificates = [];
        if (cookies.get(COOKIES_CART)) {
            cookies.get(COOKIES_CART).split(',')
                .forEach(id => getCertificateById(id, this.props).then(response => {
                    if (!response.ok) {
                        Alert.error(getMessage(props, 'certificateUnavailable'));
                    } else {
                        response.json().then(json => this.setState({
                            basketCertificates: [...this.state.basketCertificates, json.giftCertificate]
                        }))
                    }
                }));
        } else  {
            this.setState({
                basketCertificates: certificates
            })

        }

    }

    componentWillMount() {
        document.title = 'Certificates'
    }

    render() {


        if (this.state.loading) {
            return <LoadingIndicator/>
        }
        return (
            <Router>
                <CookiesProvider>
                    <div className="app">
                        <div className="fixed-top">
                            <AppHeader authenticated={this.state.authenticated}
                                       currentUser={this.state.currentUser}
                                       currentRouteCertificates={this.state.currentRouteCertificates}
                                       onLogout={this.handleLogout}
                                       onLocale={this.handleLocale}
                                       basketCertificates={this.state.basketCertificates}
                                       onRemoveFromBasket={this.onRemoveFromBasket}
                                       onRefreshBasket={this.onRefreshBasket}
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
                                <PrivateRoute path="/profile"
                                              authenticated={this.state.authenticated}
                                              currentUser={this.state.currentUser}
                                              routeHandler={this.routeHandler}
                                              locale={this.state.locale}
                                              component={Profile}/>
                                <Route path={["/certificates", "/orders/self"]}
                                       render={(props) => <Certificates loginHandler={this.loginHandler}
                                                                        routeHandler={this.routeHandler}
                                                                        authenticated={this.state.authenticated}
                                                                        onAddToBasket={this.onAddToBasket}
                                                                        {...props}
                                       />}/>
                                <Route path="/login"
                                       render={(props) => <Login authenticated={this.state.authenticated}
                                                                 loginHandler={this.loginHandler}
                                                                 routeHandler={this.routeHandler}
                                                                 locale={this.state.locale}
                                                                 {...props} />}/>
                                <Route path="/signup"
                                       render={(props) => <Signup
                                           authenticated={this.state.authenticated}
                                           signUpHandler={this.signUpHandler}
                                           routeHandler={this.routeHandler}
                                           {...props} />}/>
                                <PrivateRoute
                                    path={["/add", "/edit"]}
                                    authenticated={this.state.authenticated}
                                    component={EditCertificate}
                                    routeHandler={this.routeHandler}

                                />
                                <Route path="/oauth2"
                                       loginHandler={this.loginHandler}
                                       component={OAuth2RedirectHandler}/>
                                <PrivateRoute path="/delete"
                                              component={DeleteLink}/>
                                <Route render={(props) => <NotFound
                                    locale={this.state.locale}{...props}/>}/>
                            </Switch>
                        </div>
                        <AppFooter
                            locale={this.state.locale}
                        />
                        <Alert stack={{limit: 5}}
                               timeout={5000}
                               position='top-right'
                               effect='slide'
                               offset={65}/>
                    </div>
                </CookiesProvider>
            </Router>
        );
    }
}

export default withCookies(App);

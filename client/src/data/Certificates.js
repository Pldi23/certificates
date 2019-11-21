import React, {Component} from 'react';
import {
    Container, Jumbotron, Col, Row
} from 'reactstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import {getCertificatesByHref, getOrdersSelf} from '../service/APIService';
import {withCookies} from 'react-cookie';
import LoadingIndicator from "../common/LoadingIndicator";
import {getMessage} from "../app/Message";
import Search from "./Search";
import Pages from "./Pages";
import PageSize from "./PageSize";
import './Certificates.css';
import {
    ACCESS_TOKEN,
    CERTIFICATES_DEFAULT_REQUEST_URL,
    CERTIFICATES_HREF,
    ORDERS_SELF_URL, PAGE_API_PARAMETER, SEARCH_PARAMETERS,
    SIZE_API_PARAMETER
} from "../constants";
import CertificatesListSelector from "./CertificatesListSelector";
import OrdersCertificatesList from "./OrdersCertificatesList";
import CertificatesList from "./CertificatesList";
import Alert from "react-s-alert";
import * as PropTypes from "prop-types";

class Certificates extends Component {

    static propTypes = {
        loginHandler: PropTypes.func.isRequired,
        routeHandler: PropTypes.func.isRequired,
        authenticated: PropTypes.bool.isRequired,
        onAddToBasket: PropTypes.func.isRequired,
    };


    constructor(props) {
        super(props);
        this.state = {
            certificates: [],
            links: [],
            loading: false,
            orders: [],
            showOrders: false,
            url: '',
        };
    }

    componentDidMount() {
        window.addEventListener("popstate", () => this.popStateListener());
        this.setState({
            loading: true,

        });

        let url = new URL(window.location);
        let href = url.pathname + url.search;
        this.setStateFromUrl(href);
        this.props.routeHandler(true);
    }

    componentWillUnmount() {
        window.removeEventListener("popstate", () => this.popStateListener());
    }

    setStateFromUrl(href) {
        if (href.includes(ORDERS_SELF_URL)) {
            getOrdersSelf(this.props, href)
                .then(json => {
                    this.setState({
                        orders: json.orders,
                        showOrders: true,
                        links: json._links,
                        loading: false,
                        url: href
                    })
                })
                .catch(error => {
                    this.setState({
                        loading: false
                    });
                    console.log(error)
                });
        } else {
            getCertificatesByHref(this.props, href)
                .then(json => {
                    this.setState({
                        certificates: json.certificates,
                        links: json._links,
                        loading: false,
                        url: href
                    });
                    if (this.state.links.length > 0) {
                        this.state.links.pages.forEach(page => {
                            if (page.current) {
                                localStorage.setItem(CERTIFICATES_HREF, page.href)
                            }
                        });
                    }
                })
                .catch(error => {
                    this.setState({
                        loading: false
                    });
                    console.log(error)
                });
        }
    }

    popStateListener = () => {
        this.setState({
            loading: true,
        });
        let url = new URL(window.location);
        let href = url.pathname + url.search;

        if (href.includes(ORDERS_SELF_URL)) {
            getOrdersSelf(this.props, href)
                .then(json => {
                    this.setState({
                        orders: json.orders,
                        showOrders: true,
                        links: json._links,
                        loading: false,
                    })
                })
                .catch(error => {
                    this.setState({
                        loading: false
                    });
                    console.log(error)
                });
        }
        if (href.includes('/certificates')) {
            getCertificatesByHref(this.props, href)
                .then(json => {
                    this.setState({
                        certificates: json.certificates,
                        links: json._links,
                        loading: false,
                    });
                })
                .catch(error => {
                    this.setState({
                        loading: false
                    });
                    console.log(error)
                });
        }
    };

    pageSizeHandler = (value) => {
        this.setState({
            loading: true,
        });
        let href = this.state.url;
        href = href.includes(SIZE_API_PARAMETER) ? href.replace(/size=\d+/, SIZE_API_PARAMETER + value) :
            href.concat('&size=' + value);
        href = href.includes(PAGE_API_PARAMETER) ? href.replace(/page=\d+/, 'page=1') :
            href.concat('&page=1');
        localStorage.setItem(CERTIFICATES_HREF, href);
        if (href.includes(ORDERS_SELF_URL)) {
            getOrdersSelf(this.props, href)
                .then(json => {
                    this.setState({
                        loading: false,
                        orders: json.orders,
                        showOrders: true,
                        links: json._links,
                        url: href
                    })
                })
                .catch(error => {
                    this.setState({
                        loading: false
                    });
                    console.log(error)
                });
        } else {
            getCertificatesByHref(this.props, href)
                .then(json => {
                    this.setState({
                        certificates: json.certificates,
                        links: json._links,
                        loading: false,
                        url: href
                    });
                })
                .catch(error => {
                    this.setState({
                        loading: false
                    });
                    console.log(error)
                });
        }
        this.props.history.push(href)
    };

    pageHandler = (href) => {
        this.setState({
            loading: true,
        });
        localStorage.setItem(CERTIFICATES_HREF, href);

        getCertificatesByHref(this.props, href).then(json => {
            if (href.includes(ORDERS_SELF_URL)) {
                this.setState({
                    orders: json.orders ? json.orders : [],
                    showOrders: true,
                    links: json._links,
                    loading: false,
                    url: href
                })
            } else {
                this.setState({
                    certificates: json.certificates ? json.certificates : [],
                    links: json._links,
                    loading: false,
                    url: href
                });

            }
        })
            .catch(error => {
                this.setState({
                    loading: false
                });
                console.log(error)
            });
        this.props.history.push(href)

    };

    getCurrentPageSize() {
        let url = new URL(window.location);
        let href = url.pathname + url.search;
        href = href.includes('size') ? href.substring(href.indexOf('size=') + 5) : '5';
        href = href.includes('&') ? href.substring(0, href.indexOf('&')) : href;
        return href;
    }

    reloadHandler = () => {
        this.setState({
            loading: true
        });
        let url = new URL(window.location);
        let href = url.pathname + url.search;

        getCertificatesByHref(this.props, href)
            .then(json => {
                this.setState({
                    certificates: json.certificates,
                    links: json._links,
                    loading: false,
                    url: href
                });
            })
            .catch(error => {
                this.setState({
                    loading: false
                });
                console.log(error)
            });
        this.props.history.push(href)

    };

    selectCertificatesHandler = () => {
        this.setState({
            loading: true
        });
        let href = CERTIFICATES_DEFAULT_REQUEST_URL;
        localStorage.setItem(CERTIFICATES_HREF, href);
        getCertificatesByHref(this.props, href)
            .then(json => {
                this.setState({
                    certificates: json.certificates,
                    links: json._links,
                    loading: false,
                    showOrders: false,
                    url: href
                });
            })
            .catch(error => {
                this.setState({
                    loading: false
                });
                console.log(error)
            });
        this.props.history.push(href)

    };

    ordersHandler = () => {
        this.setState({
            loading: true
        });
        let href = '/orders/self?page=1&size=' + this.getCurrentPageSize();
        localStorage.setItem(CERTIFICATES_HREF, href);
        getOrdersSelf(this.props, href)
            .then(json => {
                this.setState({
                    loading: false,
                    orders: json.orders,
                    showOrders: true,
                    links: json._links,
                    url: href
                })
            })
            .catch(error => {
                this.setState({
                    loading: false
                });
                console.log(error)
            });
        this.props.history.push(href)


    };

    tagSearchHandler = (tagName) => {
        this.setState({
            loading: true,
        });
        localStorage.removeItem(SEARCH_PARAMETERS);
        let href = CERTIFICATES_DEFAULT_REQUEST_URL + '&tag_name=' + tagName + '&page=1&size=' + this.getCurrentPageSize();
        localStorage.setItem(CERTIFICATES_HREF, href);
        getCertificatesByHref(this.props, href).then(json => {
            this.setState({
                certificates: json.certificates ? json.certificates : [],
                links: json._links,
                loading: false,
                showOrders: false,
                url: href
            });
        })
            .catch(error => {
                this.setState({
                    loading: false
                });
                console.log(error)
            });
        this.props.history.push(href)

    };

    getSearchValue = () => {
        const queryString = require('query-string');
        const parsed = queryString.parse(window.location.search);
        let query = '';
        if (parsed.name && parsed.description) {
            if (!parsed.name.startsWith('l:') || parsed.description !== parsed.name ) {
                Alert.error(getMessage(this.props, 'invalidQuery'));
                return '';
            }
            query = query.concat(parsed.name.replace('l:', ''));
        }
        if (parsed.tag_name) {
            let array = parsed.tag_name.split(',');
            array.forEach(value => query = query.concat(' #(' + value + ')'))
        }
        if (parsed.price) {
            let price = parsed.price;
            if (price.startsWith('bw:')) {
                price = price.replace('bw:', '');
                price = price.replace(',','-');
                price = price.startsWith('0-') ? price.replace('0-', '<') : price;
                if ( price.endsWith('-9007199254740991')) {
                    price = '>' + price.replace('-9007199254740991', '')
                }
            }
            query = query + ' $(' + price + ')'
        }
        return query

    };

    render() {
        if (this.state.loading) {
            return <LoadingIndicator/>
        }
        const query = this.getSearchValue();

        return <div>
            <Jumbotron fluid>
                <Container fluid>
                    <h1 className="display-6 text-center">{getMessage(this.props, 'certificatesLabel')}</h1>
                    <Col sm={{size: 9, offset: 3}}>
                        <Row>
                            {localStorage.getItem(ACCESS_TOKEN) ? (
                                <CertificatesListSelector
                                    reloadHandler={this.selectCertificatesHandler}
                                    ordersHandler={this.ordersHandler}
                                    selected={this.state.showOrders}
                                />
                            ) : (null)}
                            {this.state.showOrders ? (
                                null
                            ) : (
                                <Search
                                    searchValue={query}
                                    pageHandler={this.pageHandler}
                                    size={this.getCurrentPageSize()}/>

                            )}
                        </Row>
                    </Col>

                </Container>
            </Jumbotron>
            {this.state.certificates.length > 0 || this.state.orders.length ? (
                <Container>
                    <div>
                        {this.state.showOrders ? (
                            <OrdersCertificatesList
                                orders={this.state.orders}
                                locale={this.props.cookies.cookies.locale}
                                reloadHandler={this.reloadHandler}
                                tagSearchHandler={this.tagSearchHandler}
                                onAddToBasket={this.props.onAddToBasket}
                            />
                        ) : (
                            < CertificatesList
                                certificates={this.state.certificates}
                                locale={this.props.cookies.cookies.locale}
                                reloadHandler={this.reloadHandler}
                                tagSearchHandler={this.tagSearchHandler}
                                onAddToBasket={this.props.onAddToBasket}
                            />
                        )
                        }
                    </div>
                    <Col sm="12" md={{size: 10, offset: 3}}>
                        <Row>
                            <Col sm={{size: 'auto', offset: 1}}>
                                <PageSize pageSizeHandler={this.pageSizeHandler} size={this.getCurrentPageSize()}/>
                            </Col>
                            <Col sm={{size: 'auto'}}>
                                <Pages pageHandler={this.pageHandler} links={this.state.links}
                                       locale={this.props.cookies.cookies.locale}/>
                            </Col>
                        </Row>
                    </Col>
                </Container>
            ) : (
                <div>
                    <Container fluid>
                        <p className="text-info">
                            {getMessage(this.props, 'noCertificates')}
                        </p>
                    </Container>
                </div>
            )}
        </div>
    }
}

export default withCookies(Certificates);
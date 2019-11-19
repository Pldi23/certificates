import React, {Component} from 'react';
import {
    Container, Jumbotron, Col, CardColumns, Row, Alert
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
import Certificate from "./Certificate";
import CertificatesListSelector from "./CertificatesListSelector";
import OrdersCertificatesList from "./OrdersCertificatesList";

class Certificates extends Component {

    constructor(props) {
        super(props);
        this.state = {
            certificates: [],
            links: [],
            loading: false,
            orders: [],
            showOrders: false,
            url: window.location.pathname
            // url: localStorage.getItem(CERTIFICATES_HREF) ? localStorage.getItem(CERTIFICATES_HREF) : CERTIFICATES_DEFAULT_REQUEST_URL
        };
    }

    componentDidMount() {
        this.setState({
            loading: true,

        });
        let href = localStorage.getItem(CERTIFICATES_HREF) ? localStorage.getItem(CERTIFICATES_HREF) : CERTIFICATES_DEFAULT_REQUEST_URL;
        // let href = this.state.url;
        if (href.includes(ORDERS_SELF_URL)) {
            getOrdersSelf(this.props, href)
                .then(json => {
                    this.setState({
                        orders: json.orders,
                        showOrders: true,
                        links: json._links,
                        loading: false,
                        url: window.location.pathname
                    })
                })
        } else {
            getCertificatesByHref(this.props, href)
                .then(json => {
                    this.setState({
                        certificates: json.certificates,
                        links: json._links,
                        loading: false,
                        url: window.location.pathname
                    });
                    if (this.state.links.length > 0) {
                        this.state.links.pages.forEach(page => {
                            if (page.current) {
                                localStorage.setItem(CERTIFICATES_HREF, page.href)
                            }
                        });
                    }
                });
        }

        this.props.routeHandler(true);
    }

    // componentDidUpdate(prevProps, prevState, snapshot) {
    //
    //     console.log('certificates updated')
    //     console.log(this.state.url)
    //     if (prevState.url !== this.state.url) {
    //         window.history.pushState(this.state, '', this.state.url)
    //
    //     }
    // }

    pageSizeHandler = (value) => {
        this.setState({
            loading: true,
        });
        let href = localStorage.getItem(CERTIFICATES_HREF) ? localStorage.getItem(CERTIFICATES_HREF) : CERTIFICATES_DEFAULT_REQUEST_URL;
        // let href = this.state.url;
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
                        url: window.location.pathname
                    })
                })
        } else {
            getCertificatesByHref(this.props, href)
                .then(json => {
                    this.setState({
                        certificates: json.certificates,
                        links: json._links,
                        loading: false,
                        url: window.location.pathname
                    });
                });
        }
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
                    url: window.location.pathname
                })
            } else {
                this.setState({
                    certificates: json.certificates ? json.certificates : [],
                    links: json._links,
                    loading: false,
                    url: window.location.pathname
                });

            }
        }).catch(error => {
            (error && error.message) ||
            getMessage(this.props, 'error');
        });
    };

    getCurrentPageSize() {
        let href = localStorage.getItem(CERTIFICATES_HREF) ? localStorage.getItem(CERTIFICATES_HREF) : CERTIFICATES_DEFAULT_REQUEST_URL;
        href = href.includes('size') ? href.substring(href.indexOf('size=') + 5) : '5';
        href = href.includes('&') ? href.substring(0, href.indexOf('&')) : href;
        return href;
    }

    reloadHandler = () => {
        this.setState({
            loading: true
        });
        let href = localStorage.getItem(CERTIFICATES_HREF) ? localStorage.getItem(CERTIFICATES_HREF) : CERTIFICATES_DEFAULT_REQUEST_URL;
        getCertificatesByHref(this.props, href)
            .then(json => {
                this.setState({
                    certificates: json.certificates,
                    links: json._links,
                    loading: false,
                    url: window.location.pathname
                });
            });

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
                    url: window.location.pathname
                });
            });

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
                    url: window.location.pathname
                })
            })


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
                url: window.location.pathname
            });
        }).catch(error => {
            (error && error.message) ||
            getMessage(this.props, 'error');
        });

    };

    render() {
        if (this.state.loading) {
            return <LoadingIndicator/>
        }
        console.log(this.state.url)
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
                                <Search pageHandler={this.pageHandler} size={this.getCurrentPageSize()}/>

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

                                options={this.state.options}
                                reloadHandler={this.reloadHandler}
                                tagSearchHandler={this.tagSearchHandler}
                                onAddToBasket={this.props.onAddToBasket}
                            />
                        ) : (
                            < CertificatesList
                                certificates={this.state.certificates}
                                locale={this.props.cookies.cookies.locale}
                                options={this.state.options}
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
                        <Alert color="info">
                            {getMessage(this.props, 'noCertificates')}
                        </Alert>
                    </Container>
                </div>
            )}
        </div>
    }
}

class CertificatesList extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            certificates: this.props.certificates
        }
    }

    render() {
        const certificates = this.state.certificates.map((giftCertificate) =>

            <Certificate
                key={giftCertificate.giftCertificate.id}
                certificate={giftCertificate}
                locale={this.props.locale}
                reloadHandler={this.props.reloadHandler}
                tagSearchHandler={this.props.tagSearchHandler}
                onAddToBasket={this.props.onAddToBasket}
                showButtons={true}
            />
        );
        return (
            <CardColumns>
                {certificates}
            </CardColumns>
        )
    }
}


export default withCookies(Certificates);
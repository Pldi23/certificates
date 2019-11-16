import React, {Component} from 'react';
import {
    Container, Jumbotron, Col, CardColumns, Row, Alert
} from 'reactstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import {getCertificatesByHref} from '../../util/APIUtils';
import {withCookies} from 'react-cookie';
import LoadingIndicator from "../../common/LoadingIndicator";
import {getMessage} from "../../app/Message";
import Search from "./Search";
import Pages from "./Pages";
import PageSize from "./PageSize";
import './Certificates.css';
import {CERTIFICATES_DEFAULT_REQUEST_URL, CERTIFICATES_HREF} from "../../constants";
import Certificate from "./Certificate";

class Certificates extends Component {

    constructor(props) {
        super(props);
        this.state = {
            certificates: [],
            links: [],
            loading: false,
        };
    }

    componentDidMount() {
        this.setState({
            loading: true,

        });
        let href = localStorage.getItem(CERTIFICATES_HREF);
        getCertificatesByHref(this.props, href ? href : CERTIFICATES_DEFAULT_REQUEST_URL)
            .then(json => {
                this.setState({
                    certificates: json.certificates,
                    links: json._links,
                    loading: false
                });
                if (this.state.links.length > 0) {
                    this.state.links.pages.map(page => {
                        if (page.current) {
                            localStorage.setItem(CERTIFICATES_HREF, page.href)
                        }
                    });
                }
            });

        this.props.routeHandler(true);
    }

    componentWillUnmount() {
        localStorage.removeItem(CERTIFICATES_HREF)
    }

    pageSizeHandler = (value) => {
        this.setState({
            loading: true,

        });
        let href = localStorage.getItem(CERTIFICATES_HREF) ? localStorage.getItem(CERTIFICATES_HREF) : CERTIFICATES_DEFAULT_REQUEST_URL;

        console.log('size handler before')
        console.log(href)
        console.log(value)

        if (href.includes('size=')) {
            console.log('replacing')
            href = href.replace(/size=\d+/, 'size=' + value);

        } else {
            console.log('concate')

            href = href.concat('&size=' + value);

        }

        href = href.includes('page=') ? href.replace(/page=\d+/, 'page=1') :
            href.concat('&page=1');


        console.log('size handler after')
        console.log(href)

        localStorage.setItem(CERTIFICATES_HREF, href);
        getCertificatesByHref(this.props, href)
            .then(json => {
                this.setState({
                    certificates: json.certificates,
                    links: json._links,
                    loading: false
                });
            });
    };

    pageHandler = (href) => {
        this.setState({
            loading: true,
        });
        localStorage.setItem(CERTIFICATES_HREF, href);
        getCertificatesByHref(this.props, href).then(json => {
            console.log(json)
            this.setState({
                certificates: json.certificates ? json.certificates : [],
                links: json._links,
                loading: false
            });
        }).catch(error => {
            (error && error.message) ||
            getMessage(this.props, 'error');
        });
    };

    getCurrentPageSize() {
        let href = localStorage.getItem(CERTIFICATES_HREF) ? localStorage.getItem(CERTIFICATES_HREF) : CERTIFICATES_DEFAULT_REQUEST_URL;
        // console.log('get current size');
        // console.log(href);

        href = href.includes('size') ? href.substring(href.indexOf('size=') + 5) : '5';
        href = href.includes('&') ? href.substring(0, href.indexOf('&')) : href;
        // console.log(href.indexOf('&'));
        return href;
    }

    reloadHandler = () => {
        this.setState({
            loading: true
        });
        let href = localStorage.getItem(CERTIFICATES_HREF);
        // getCertificatesByQuery(this.props, this.state.options)
        getCertificatesByHref(this.props, href)
            .then(json => {
                this.setState({
                    certificates: json.certificates,
                    links: json._links,
                    loading: false
                });
            });
    };

    tagSearchHandler = (tagName) => {
        this.setState({
            loading: true,
        });
        let href = CERTIFICATES_DEFAULT_REQUEST_URL + '&tag_name=' + tagName + '&page=1&size=' + this.getCurrentPageSize();
        localStorage.setItem(CERTIFICATES_HREF, href);
        console.log('handling tag search')
        getCertificatesByHref(this.props, href).then(json => {
            console.log(json)
            this.setState({
                certificates: json.certificates ? json.certificates : [],
                links: json._links,
                loading: false
            });
        }).catch(error => {
            (error && error.message) ||
            getMessage(this.props, 'error');
        });
    };

    buyHandler = () => {

    }


    render() {
        if (this.state.loading) {
            return <LoadingIndicator/>
        }
        console.log(this.state.certificates.length)
        return <div>
            <Jumbotron fluid>
                <Container fluid>
                    <h1 className="display-6 text-center">{getMessage(this.props, 'certificatesLabel')}</h1>
                    <Search pageHandler={this.pageHandler} size={this.getCurrentPageSize()}/>
                </Container>
            </Jumbotron>
            {this.state.certificates.length > 0 ? (
                <Container>
                    <div>
                        <CertificatesList
                            certificates={this.state.certificates}
                            locale={this.props.cookies.cookies.locale}
                            options={this.state.options}
                            reloadHandler={this.reloadHandler}
                            tagSearchHandler={this.tagSearchHandler}
                            buyHandler={this.buyHandler}
                        />
                    </div>
                    <Col sm="12" md={{size: 10, offset: 3}}>
                        <Row>
                            <Col sm={{size: 'auto', offset: 1}}>
                                <PageSize pageSizeHandler={this.pageSizeHandler} size={this.getCurrentPageSize()}/>
                            </Col>
                            <Col sm={{size: 'auto'}}>
                                <Pages pageHandler={this.pageHandler} links={this.state.links}/>
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
        const certificates = this.state.certificates.map(giftCertificate =>

            <Certificate
                key={giftCertificate.giftCertificate.id}
                certificate={giftCertificate}
                locale={this.props.locale}
                reloadHandler={this.props.reloadHandler}
                tagSearchHandler={this.props.tagSearchHandler}
                buyHandler={this.props.buyHandler}
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
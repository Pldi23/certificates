import React, {Component} from 'react';
import {
    Container, Form, FormGroup, Jumbotron, Input, Label, Button, Col, Card, CardBody,
    CardTitle, CardText, CardHeader, CardFooter, CardColumns, Badge, Row
} from 'reactstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import {search, getCertificates} from '../../util/APIUtils';
import Alert from 'react-s-alert';
import {withCookies} from 'react-cookie';
import LoadingIndicator from "../../common/LoadingIndicator";
import {ACCESS_TOKEN, API_BASE_URL} from "../../constants";

class Certificates extends Component {
    state = {
        certificates: [],
        loading: false
    };

    constructor(props) {
        super(props);

        const {cookies} = props;
        this.state.csrfToken = cookies.get('XSRF-TOKEN');
    }

    componentDidMount() {
        this.setState({
            loading: true
        });
        getCertificates(this.state.csrfToken, this.props).then(response => {
            this.setState({
                certificates: response.certificates,
                loading: false
            });
        });
        // this.props.loginHandler(true);
        // if (localStorage.getItem(ACCESS_TOKEN) && localStorage.getItem(ACCESS_TOKEN) != null) {
        //     if (localStorage.getItem(ACCESS_TOKEN_EXPIRES_IN) && localStorage.getItem(REFRESH_TOKEN) &&
        //         localStorage.getItem(ACCESS_TOKEN_EXPIRES_IN) < Date.now()) {
        //         console.log('certificates refreshing token start, expired bearer: ' + localStorage.getItem(ACCESS_TOKEN));
        //         fetch(API_BASE_URL + '/authenticate/refresh-token', {
        //             method: 'POST',
        //             credentials: 'include',
        //             headers: {
        //                 'RefreshToken': localStorage.getItem(REFRESH_TOKEN),
        //                 'X-XSRF-TOKEN': this.state.csrfToken,
        //                 'Accept': 'application/json',
        //                 'Content-Type': 'application/json',
        //             },
        //         }).then(response => {
        //             if (response.ok) {
        //                 localStorage.setItem(ACCESS_TOKEN, response.headers.get("Authorization"));
        //                 localStorage.setItem(REFRESH_TOKEN, response.headers.get("RefreshToken"));
        //                 localStorage.setItem(ACCESS_TOKEN_EXPIRES_IN, response.headers.get("ExpiresIn"));
        //                 console.log('response ok new access token set ' + localStorage.getItem(ACCESS_TOKEN));
        //                 this.props.loginHandler(true);
        //                 return this.fetchCertificates();
        //
        //             } else {
        //                 console.log('respons not ok, something go wrong ' + response.status + ' , ' + response + ' , ' + localStorage.getItem(ACCESS_TOKEN));
        //             }
        //         }).catch(error => {
        //             console.log(
        //                 (error && error.message) || 'could not refresh token');
        //         });
        //     } else {
        //         console.log(' no refreshing token needed (token not expired)' + localStorage.getItem(ACCESS_TOKEN));
        //         return this.fetchCertificates()
        //     }
        // } else {
        //     console.log(' no refreshing token needed (no token?)' + localStorage.getItem(ACCESS_TOKEN));
        //     return this.fetchCertificates();
        //
        // }

    }

    fetchCertificates() {
        fetch(API_BASE_URL + '/certificates', {
            method: 'GET',
            headers: {
                'Authorization': localStorage.getItem(ACCESS_TOKEN),
                'Content-Type': 'application/json',
            }
        }).then(response => {
                return response.json().then(json => {
                    if (!response.ok) {
                        return Promise.reject(json);
                    }
                    return json;
                });
            }
        ).then(response => {
            this.setState({
                certificates: response.certificates,
                loading: false
            });
        });
    }

    render() {
        if (this.state.loading) {
            return <LoadingIndicator/>
        }

        return <div>
            <Jumbotron fluid>
                <Container fluid>
                    <h1 className="display-6 text-center">Certificates</h1>
                    <Search/>
                </Container>
            </Jumbotron>
            <Container>
                <div>
                    <CertificatesList certificates={this.state.certificates}/>
                </div>
            </Container>
        </div>
    }
}

class CertificatesList extends React.Component {
    render() {
        const certificates = this.props.certificates.map(giftCertificate =>
            <Certificate key={giftCertificate.giftCertificate.id} certificate={giftCertificate}/>
        );
        return (
            <CardColumns>
                {certificates}
            </CardColumns>
        )
    }
}

class Certificate extends React.Component {
    render() {
        const tags = this.props.certificate.tags.map(tag =>
            <Tag key={tag.id} tag={tag}/>
        );

        const editLink = this.props.certificate._links.update;
        const deleteLink = this.props.certificate._links.delete;
        const buyLink = this.props.certificate._links.self;
        return (
            <Card body className="text-center">
                <CardHeader>
                    <Row>
                        <Col className="text-left">
                            {this.props.certificate.giftCertificate.name}
                        </Col>
                        <Col className="text-right">
                            {this.props.certificate.giftCertificate.creationDate}
                        </Col>
                    </Row>

                </CardHeader>
                <CardBody>
                    <CardTitle className="text-left">{this.props.certificate.giftCertificate.descroption}</CardTitle>
                    <CardText>{this.props.certificate.giftCertificate.description}</CardText>
                    <CardText>Expiring in: {this.props.certificate.giftCertificate.expirationDate}</CardText>
                    <CardText>Tags: {tags}</CardText>
                </CardBody>
                <CardFooter>
                    <Row>
                        <HateoasEditLink className="float-left" link={editLink}/>
                        <HateoasDeleteLink className="float-left" link={deleteLink}/>
                        <Col className="text-right float-right">
                            <HateoasBuyLink link={buyLink}/>
                            <Badge color="success">{this.props.certificate.giftCertificate.price}$</Badge>
                        </Col>
                    </Row>
                </CardFooter>
            </Card>
        )
    }
}

class Tag extends React.Component {
    render() {
        return <Badge color="info">{this.props.tag.tag.title} </Badge>
    }
}

class HateoasEditLink extends React.Component {
    render() {
        if (this.props.link !== undefined) {
            return <Button color="link">Edit</Button>
        } else {
            return null;
        }
        //{this.props.link.type} : {this.props.link.href}
    }
}

class HateoasDeleteLink extends React.Component {
    render() {
        if (this.props.link !== undefined) {
            return <Button color="link">Delete</Button>
        } else {
            return null;
        }
    }
}

class HateoasBuyLink extends React.Component {
    render() {
        if (this.props.link !== undefined) {
            return <Button color="link">Buy</Button>
        } else {
            return null;
        }
    }
}


class Search extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            params: '',
            message: '',
        };
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

        search(this.state.params)
            .then(response => {
                if (response.ok) {
                    console.log(response.body);
                    // Alert.success("You're successfully logged in!");
                    // this.props.history.push("/certificates");
                } else {
                    response.json().then(response => {
                        let message = JSON.stringify(response.messages);
                        Alert.error(message.substring(1, message.length - 1))
                    });
                }
            })
            .catch(error => {
                Alert.error(
                    (error && error.message) ||
                    'Oops! Something went wrong. Please try again!');
            });
    }

    render() {
        return <Form inline onSubmit={this.handleSubmit}>
            <Col sm="12" md={{size: 9, offset: 5}}>
                <FormGroup>
                    <Label for="search" hidden>Search</Label>
                    <Input type="text" name="search" id="search" placeholder="search"
                           value={this.state.params} onChange={this.handleInputChange} required/>
                    {' '}
                    <Button outline color="secondary">Go!</Button>
                </FormGroup>
            </Col>
        </Form>
    }

}

export default withCookies(Certificates);
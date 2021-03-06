import React from 'react';
import {withCookies} from 'react-cookie';
import {WithContext as ReactTags} from 'react-tag-input';
import {Button, Col, Container, Jumbotron, Label} from "reactstrap";
import {AvFeedback, AvForm, AvGroup, AvInput} from 'availity-reactstrap-validation';
import {
    deleteCertificate,
    getCertificateById,
    getTags,
    loadCertificate,
    updateCertificate
} from "../service/APIService";
import Alert from "react-s-alert";
import './CreateCertificate.css';
import {getMessage} from "../app/Message";
import {Prompt} from "react-router-dom";
import {
    CERTIFICATE_NAME_REGEX_PATTERN,
    CERTIFICATE_PRICE_PATTERN,
    CERTIFICATES_DEFAULT_REQUEST_URL
} from "../constants";
import confirm from "reactstrap-confirm";

const KeyCodes = {
    comma: 188,
    enter: 13,
};

const delimiters = [KeyCodes.comma, KeyCodes.enter];

class EditCertificate extends React.Component {

    constructor(props) {
        super(props);


        this.state = {
            name: {
                value: '',
                isValid: false
            },
            description: {
                value: '',
                isValid: false
            },
            price: {
                value: '',
                isValid: false
            },
            expiration: {
                value: ''
            },
            tags: [],
            suggestions: [],
            loading: false,
            link: '',
            isBlocking: false,
            id: null
        };
    }


    componentDidMount() {
        this.setState({
            loading: true
        });
        this.setState(this.props.location.state);
        getTags(this.props).then(json => {
            this.setState({
                suggestions: json.tagList.map(data => {
                    return {
                        id: data.tag.title,
                        text: data.tag.title
                    }
                }),
                loading: false
            });
        });

        this.props.routeHandler(false);
    }

    handleInputName = (event) => {
        if (event.target.value.match(CERTIFICATE_NAME_REGEX_PATTERN) &&
            event.target.value.length > 0 && event.target.value.length <= 30) {
            this.setState({
                name: {
                    value: event.target.value,
                    isValid: true
                },
                isBlocking: event.target.value.length > 0
            })
        } else {
            this.setState({
                name: {
                    value: event.target.value,
                    isValid: false
                },
                isBlocking: event.target.value.length > 0
            })
        }
    };

    handleInputPrice = (event) => {
        if (event.target.value.match(CERTIFICATE_PRICE_PATTERN) && event.target.value >= 0) {
            this.setState({
                price: {
                    value: event.target.value,
                    isValid: true
                },
                isBlocking: event.target.value.length > 0
            })
        } else {
            this.setState({
                price: {
                    value: event.target.value,
                    isValid: false
                },
                isBlocking: event.target.value.length > 0
            })
        }
    };

    handleInputDescription = (event) => {
        if (event.target.value.length > 0 && event.target.value.length < 1000) {
            this.setState({
                description: {
                    value: event.target.value,
                    isValid: true
                },
                isBlocking: event.target.value.length > 0
            })
        } else {
            this.setState({
                description: {
                    value: event.target.value,
                    isValid: false
                },
                isBlocking: event.target.value.length > 0
            })
        }
    };

    handleInputExpiration = (event) => {
        if (new Date(event.target.value) > new Date()) {
            this.setState({
                expiration: {
                    value: event.target.value,
                    isValid: true
                },
                isBlocking: event.target.value.length > 0
            })
        } else {
            this.setState({
                expiration: {
                    value: event.target.value,
                    isValid: false
                },
                isBlocking: event.target.value.length > 0
            })
        }
    };

    handleEdit = () => {
        this.setState({
            isBlocking: false
        });
        getCertificateById(this.state.id, this.props).then(async response => {
                if (!response.ok) {

                    let result = await confirm({
                        title: '',
                        message: getMessage(this.props, 'certificateWasDeleted'),
                        confirmText: getMessage(this.props, 'ok'),
                        cancelText: getMessage(this.props, 'cancel'),
                        confirmColor: "link text-danger",
                        cancelColor: "primary"
                    });
                    result ?
                        this.handleAdd()
                        : this.props.history.push('/edit');

                } else {
                    const certificateJson = this.certificateToJson();
                    updateCertificate(certificateJson, this.props, this.state.link.href).then(response => {
                        if (response.ok) {
                            response.json().then(json => {
                                Alert.success(getMessage(this.props, 'certificateUpdated'));
                            });
                            this.props.history.push(CERTIFICATES_DEFAULT_REQUEST_URL);
                        } else {
                            response.json().then(json => {
                                let message = JSON.stringify(json.messages);
                                Alert.error(message.substring(1, message.length - 1));
                            });
                        }
                    }).catch(error => {
                        Alert.error((error && error.message) || getMessage(this.props, 'error'));
                    });
                }
            }
        )
    };

    getParsedDate(date) {
        date = String(date).split('-');
        let year = String(date[0]);
        let month = String(date[1]);
        let day = String(date[2]);
        return day + '/' + month + '/' + year
    }


    certificateToJson() {
        return JSON.stringify({
            name: this.state.name.value,
            description: this.state.description.value,
            price: this.state.price.value,
            expirationDate: this.getParsedDate(this.state.expiration.value),
            tags: this.state.tags.map(tag => {
                return {
                    title: tag.text
                }
            })
        });
    }

    handleAdd = () => {
        this.setState({
            isBlocking: false
        });
        const certificateJson = this.certificateToJson();

        loadCertificate(certificateJson, this.props)
            .then(response => {
                if (response.ok) {
                    response.json().then(json => {
                        Alert.success(getMessage(this.props, 'certificateAdded'));
                    });
                    this.props.history.push(CERTIFICATES_DEFAULT_REQUEST_URL);
                } else {
                    response.json().then(json => {
                        let message = JSON.stringify(json.messages);
                        Alert.error(message.substring(1, message.length - 1));
                    });
                }
            }).catch(error => {
            Alert.error((error && error.message) || getMessage(this.props, 'error'));
        });
    };

    handleRemove = async () => {
        this.setState({
            isBlocking: false
        });
        let result = await confirm({
            title: '',
            message: getMessage(this.props, 'areYouSure'),
            confirmText: getMessage(this.props, 'ok'),
            cancelText: getMessage(this.props, 'cancel'),
            confirmColor: "link text-danger",
            cancelColor: "primary"
        });
        return result ?
            deleteCertificate(this.props, this.state.link.href).then(response => {
                if (response.ok) {
                    Alert.success(getMessage(this.props, 'certificateDeleted'));
                    this.props.history.push(CERTIFICATES_DEFAULT_REQUEST_URL)
                } else {
                    response.json().then(json => {
                        let message = JSON.stringify(json.messages);
                        Alert.error(message.substring(1, message.length - 1));
                    });
                }
            }).catch(error => {
                Alert.error((error && error.message) || getMessage(this.props, 'error'));
            }) : null;

    };


    handleDelete = (i) => {
        const {tags} = this.state;
        this.setState({
            tags: tags.filter((tag, index) => index !== i),
            isBlocking: tags.length > 0
        });
    };

    handleAddition = (tag) => {
        /(^[a-z-]+(?: [a-z-]+)+$)|(^[a-z-]+$)/.test(tag.text) ?
        this.setState(state => ({
            tags: [...state.tags, tag],
            isBlocking: true
        })) :
            Alert.error('invalid tag name')
    };

    handleDrag = (tag, currPos, newPos) => {
        const tags = [...this.state.tags];
        const newTags = tags.slice();

        newTags.splice(currPos, 1);
        newTags.splice(newPos, 0, tag);

        this.setState({tags: newTags});
    };


    render() {
        const {tags, suggestions} = this.state;
        const nameLength = this.state.name.length;
        const descriptionLength = this.state.name.description;
        let url = new URL(window.location);
        let href = url.pathname + url.search;
        const isBlocking = this.state.isBlocking;
        const isFormValid = this.state.name.isValid && this.state.description.isValid && this.state.price.isValid &&
            this.state.expiration.isValid;
        let today = new Date();
        let dd = today.getDate();
        let mm = today.getMonth()+1; //January is 0!
        let yyyy = today.getFullYear();
        let yyyyPlusTen = today.getFullYear() + 10;
        if(dd<10){
            dd='0'+dd
        }
        if(mm<10){
            mm='0'+mm
        }

        let todayPlusTenYears = yyyyPlusTen+'-'+mm+'-'+dd;
        today = yyyy+'-'+mm+'-'+dd;

        return(
            <div>
                <Prompt
                    when={isBlocking}
                    message={
                        getMessage(this.props, 'unsavedData')
                    }
                />
                <Jumbotron fluid>
                    <Container fluid>
                        <Col sm="12" md={{size: 6, offset: 3}}><h1
                            className="display-6 text-center">{getMessage(this.props, 'addEditLabel')}</h1></Col>
                    </Container>
                </Jumbotron>
                <Container>
                    <AvForm>
                        <Col sm="12" md={{size: 6, offset: 3}}>
                            <AvGroup>
                                <Label for="certificateName">{getMessage(this.props, 'title')}*</Label>
                                <AvInput
                                    type="text"
                                    name="name"
                                    id="certificateName"
                                    placeholder={getMessage(this.props, 'title')}
                                    pattern={CERTIFICATE_NAME_REGEX_PATTERN}
                                    required={true}
                                    minLength={"1"}
                                    maxLength={"30"}
                                    value={this.state.name.value}
                                    onChange={this.handleInputName}
                                />
                                <AvFeedback>{getMessage(this.props, 'titleViolation')} {nameLength}/30</AvFeedback>
                            </AvGroup>
                            <AvGroup>
                                <Label for="certificateDescription">{getMessage(this.props, 'description')}*</Label>
                                <AvInput
                                    type="text"
                                    name="description"
                                    id="certificateDescription"
                                    placeholder={getMessage(this.props, 'description')}
                                    minLength="1"
                                    maxLength="1000"
                                    required={true}
                                    value={this.state.description.value}
                                    onChange={this.handleInputDescription}
                                />
                                <AvFeedback>{getMessage(this.props, 'descriptionViolation')} {descriptionLength}/1000</AvFeedback>
                            </AvGroup>
                            <AvGroup>
                                <Label for="certificatePrice">{getMessage(this.props, 'price')}*</Label>
                                <AvInput
                                    type="number"
                                    name="price"
                                    id="certificatePrice"
                                    placeholder={getMessage(this.props, 'price')}
                                    pattern={CERTIFICATE_PRICE_PATTERN}
                                    min={"0"}
                                    step="0.01"
                                    required={true}
                                    value={this.state.price.value}
                                    onChange={this.handleInputPrice}
                                />
                                <AvFeedback>{getMessage(this.props, 'priceViolation')}</AvFeedback>
                            </AvGroup>
                            <AvGroup>
                                <Label for="certificateExpiration">{getMessage(this.props, 'expiration')}*</Label>
                                <AvInput
                                    type="date"
                                    name="expiration"
                                    id="certificateExpiration"
                                    placeholder={getMessage(this.props, 'expiration')}
                                    min={today}
                                    max={todayPlusTenYears}
                                    required={true}
                                    value={this.state.expiration.value}
                                    onChange={this.handleInputExpiration}
                                />
                                <AvFeedback>{getMessage(this.props, 'expirationViolation')}</AvFeedback>
                            </AvGroup>
                            <div>
                                <ReactTags tags={tags}
                                           suggestions={suggestions}
                                           handleDelete={this.handleDelete}
                                           handleAddition={this.handleAddition}
                                           handleDrag={this.handleDrag}
                                           delimiters={delimiters}
                                           maxLength={15}
                                           inputFieldPosition="top"
                                           placeholder={getMessage(this.props, 'addNewTag')}
                                           renderSuggestion={({text}, query) => <div style={{
                                               textDecoration: 'underline',
                                               textDecorationStyle: 'wavy',
                                           }}>{text} ({query})</div>
                                           }
                                />
                            </div>
                        </Col>
                        <Col sm="12" md={{size: 7, offset: 7}}>
                            <div>
                                {href.includes('/add') ? (
                                    <Button disabled={!isFormValid} type="button" color="danger"
                                            onClick={this.handleAdd}>{getMessage(this.props, 'add')}</Button>
                                ) : (
                                    <div>
                                        <Button disabled={!isFormValid} type="button" color="danger"
                                                onClick={this.handleEdit}>{getMessage(this.props, 'edit')}</Button>{' '}
                                        <Button disabled={!isFormValid} type="button" onClick={this.handleRemove}
                                                color="danger">{getMessage(this.props, 'delete')}</Button>
                                    </div>
                                )}
                            </div>
                        </Col>
                    </AvForm>
                </Container>
            </div>
        )
    }
}

export default withCookies(EditCertificate);
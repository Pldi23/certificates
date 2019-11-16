import React from 'react';
import {withCookies} from 'react-cookie';
import {WithContext as ReactTags} from 'react-tag-input';
import {Button, Col, Container, Jumbotron, Label} from "reactstrap";
import {AvFeedback, AvForm, AvGroup, AvInput} from 'availity-reactstrap-validation';
import {deleteCertificate, getTags, loadCertificate, updateCertificate} from "../../util/APIUtils";
import Alert from "react-s-alert";
import './CreateCertificate.css';
import {getMessage} from "../../app/Message";
import {Prompt} from "react-router-dom";

const KeyCodes = {
    comma: 188,
    enter: 13,
};

const delimiters = [KeyCodes.comma, KeyCodes.enter];


class EditCertificate extends React.Component {


    constructor(props) {
        super(props);


        this.state = {
            name: '',
            description: '',
            price: 0,
            expiration: '',
            tags: [],
            suggestions: [],
            loading: false,
            link: '',
            isBlocking: false
        };

        const {cookies} = props;
        this.state.csrfToken = cookies.get('XSRF-TOKEN');

        this.handleInputChange = this.handleInputChange.bind(this);

        this.handleDelete = this.handleDelete.bind(this);
        this.handleAddition = this.handleAddition.bind(this);
        this.handleDrag = this.handleDrag.bind(this);
    }

    async componentDidMount() {
        this.setState(this.props.location.state);
        this.setState({
            loading: true
        });
        await getTags(this.props).then(json => {
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
        this.setState({
            loading: false
        });
        this.props.routeHandler(false);
    }

    handleInputChange(event) {
        const target = event.target;
        const inputName = target.name;
        const inputValue = target.value;

        this.setState({
            [inputName]: inputValue,
            isBlocking: target.value.length > 0
        });

    }

    handleEdit = () => {
        this.setState({
            isBlocking: false
        });
        const certificateJson = this.certificateToJson();
        updateCertificate(certificateJson, this.props, this.state.link.href).then(response => {
            if (response.ok) {
                response.json().then(json => {
                    Alert.success(getMessage(this.props, 'certificateUpdated'));
                });
                this.props.history.push("/certificates");
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

    getParsedDate(date) {
        date = String(date).split('-');
        let year = String(date[0]);
        let month = String(date[1]);
        let day = String(date[2]);
        return day + '/' + month + '/' + year
    }


    certificateToJson() {
        return JSON.stringify({
            name: this.state.name,
            description: this.state.description,
            price: this.state.price,
            expirationDate: this.getParsedDate(this.state.expiration),
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
                    this.props.history.push("/certificates");
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

    handleRemove = () => {
        this.setState({
            isBlocking: false
        });
        deleteCertificate(this.props, this.state.link.href).then(response => {
            if (response.ok) {
                Alert.success(getMessage(this.props, 'certificateDeleted'));
                this.props.history.push('/certificates')
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



    handleDelete(i) {
        const {tags} = this.state;
        this.setState({
            tags: tags.filter((tag, index) => index !== i),
        });
    }

    handleAddition(tag) {
        this.setState(state => ({tags: [...state.tags, tag]}));
    }

    handleDrag(tag, currPos, newPos) {
        const tags = [...this.state.tags];
        const newTags = tags.slice();

        newTags.splice(currPos, 1);
        newTags.splice(newPos, 0, tag);

        // re-render
        this.setState({tags: newTags});
    }


    render() {
        const {tags, suggestions} = this.state;
        const nameLength = this.state.name.length;
        const descriptionLength = this.state.name.description;
        const path = this.props.location.pathname;
        const isBlocking = this.state.isBlocking;

        return (
            <div>
                {/*<div style={*/}
                {/*    pStyle*/}
                {/*}>*/}
                <Prompt
                    when={isBlocking}
                    message={
                        getMessage(this.props, 'unsavedData')
                    }
                />
                {/*</div>*/}
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
                                <Label for="certificateName">{getMessage(this.props, 'title')}</Label>
                                <AvInput
                                    type="text"
                                    name="name"
                                    id="certificateName"
                                    placeholder={getMessage(this.props, 'title')}
                                    required
                                    pattern={"([\\w-]+(?: [\\w-]+)+)|([\\w-]+)"}
                                    minLength="1"
                                    maxLength="30"
                                    value={this.state.name}
                                    onChange={this.handleInputChange}
                                />
                                <AvFeedback>{getMessage(this.props, 'titleViolation')} {nameLength}/30</AvFeedback>
                            </AvGroup>
                            <AvGroup>
                                <Label for="certificateDescription">{getMessage(this.props, 'description')}</Label>
                                <AvInput
                                    type="text"
                                    name="description"
                                    id="certificateDescription"
                                    placeholder={getMessage(this.props, 'description')}
                                    required
                                    minLength="1"
                                    maxLength="1000"
                                    value={this.state.description}
                                    onChange={this.handleInputChange}
                                />
                                <AvFeedback>{getMessage(this.props, 'descriptionViolation')} {descriptionLength}/1000</AvFeedback>
                            </AvGroup>
                            <AvGroup>
                                <Label for="certificatePrice">{getMessage(this.props, 'price')}</Label>
                                <AvInput
                                    type="number"
                                    name="price"
                                    id="certificatePrice"
                                    placeholder={getMessage(this.props, 'price')}
                                    required
                                    pattern={"[0-9]+(.[0-9][0-9]?)?"}
                                    min={"0"}
                                    step="0.01"
                                    value={this.state.price}
                                    onChange={this.handleInputChange}
                                />
                                <AvFeedback>{getMessage(this.props, 'priceViolation')}</AvFeedback>
                            </AvGroup>
                            <AvGroup>
                                <Label for="certificateExpiration">{getMessage(this.props, 'expiration')}</Label>
                                <AvInput
                                    type="date"
                                    name="expiration"
                                    id="certificateExpiration"
                                    placeholder={getMessage(this.props, 'expiration')}
                                    required
                                    min={Date.now()}
                                    value={this.state.expiration}
                                    onChange={this.handleInputChange}
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
                                               textDecorationStyle: 'wavy'
                                           }}>{text} ({query})</div>
                                           }
                                />
                            </div>
                        </Col>
                        <Col sm="12" md={{size: 7, offset: 7}}>
                            <div>
                                <Button disabled={path !== '/add'} type="button" color="danger"
                                        onClick={this.handleAdd}>{getMessage(this.props, 'add')}</Button>{' '}
                                <Button disabled={path !== '/edit'} type="button" color="danger"
                                        onClick={this.handleEdit}>{getMessage(this.props, 'edit')}</Button>{' '}
                                {path === '/edit' ? (
                                    <Button type="button" onClick={this.handleRemove}
                                            color="danger">{getMessage(this.props, 'delete')}</Button>
                                ) : (
                                    <Button disabled type="button"
                                            color="danger">{getMessage(this.props, 'delete')}</Button>
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
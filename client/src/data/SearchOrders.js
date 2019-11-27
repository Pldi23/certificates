import {AvFeedback, AvForm, AvGroup, AvInput} from "availity-reactstrap-validation";
import {Button, Label} from "reactstrap";
import {getMessage} from "../app/Message";
import {withCookies} from "react-cookie";
import React from "react";
import Alert from "react-s-alert";
import AlertHint from "./AlertHint";
import {CERTIFICATES_DEFAULT_REQUEST_URL, ORDERS_SELF_URL} from "../constants";

const ORDERS_SEARCH_REGEX = /^[\w\s]*$/i;

class SearchOrders extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            params: {
                value: '',
                isValid: true
            }
        };
    }

    componentDidMount() {
        let value = this.props.searchValue || '';
        value === '' || ORDERS_SEARCH_REGEX.test(value) ?
            this.setState({
                params: {
                    value: value,
                    isValid: true
                }
            }) :
            this.setState({
                params: {
                    value: value,
                    isValid: false
                }
            })
    }

    handleInputChange = (event) => {
        const target = event.target;
        const inputValue = target.value;

        if (ORDERS_SEARCH_REGEX.test(inputValue)) {
            this.setState({
                params: {
                    value: inputValue,
                    isValid: true
                },
            });
        } else {
            this.setState({
                params: {
                    value: inputValue,
                    isValid: false
                },
            });
        }
    };

    handleSubmit = () => {

        if (!this.state.params.isValid) {
            Alert.info(<AlertHint hint={'ordersSearch'} {...this.props}/>, {
                timeout: 'none'
            })
        } else {
            let search = this.state.params.value.trim();
            let href = ORDERS_SELF_URL + '?page=1&size=' + this.props.size + '&c_name=' + search;
            this.props.pageHandler(href);
        }
    };

    onDragOver = (ev) => {
        ev.preventDefault();
    };

    onDrop = (ev) => {
        let data = ev.dataTransfer.getData('text');

        if (ORDERS_SEARCH_REGEX.test(data)) {
            this.setState({
                params: {
                    value: data,
                    isValid: true
                },
            });
        } else {
            this.setState({
                params: {
                    value: data,
                    isValid: false
                },
            });
        }
    };

    render() {
        return <AvForm inline onSubmit={this.handleSubmit}>
            <AvGroup>
                <Label for="search" hidden>Search</Label>
                <AvInput
                    type="text"
                    name="params"
                    id="search"
                    placeholder={getMessage(this.props, 'searchParams')}
                    maxLength="100"
                    minLength="1"
                    value={this.state.params.value}
                    onChange={this.handleInputChange}
                    className={'searchStyle'}
                    onDragOver={(e)=>this.onDragOver(e)}
                    onDrop={(e)=>{this.onDrop(e)}}
                />
                <AvFeedback>{getMessage(this.props, 'searchViolation')}</AvFeedback>
                <Button outline color="secondary">{getMessage(this.props, 'searchCommand')}</Button>
            </AvGroup>
        </AvForm>

    }
}

export default withCookies(SearchOrders);
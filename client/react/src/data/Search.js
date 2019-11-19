import React from "react";
import {Button, Label} from "reactstrap";
import {withCookies} from "react-cookie";
import {AvFeedback, AvForm, AvGroup, AvInput} from "availity-reactstrap-validation";
import {getMessage} from "../app/Message";
import {CERTIFICATES_DEFAULT_REQUEST_URL, SEARCH_PARAMETERS, SEARCH_REGEX_PATTERN} from "../constants";
import Alert from 'react-s-alert';

const pStyle = {
    width: '435px',
    marginRight: '10px',
};

class Search extends React.Component {


    constructor(props) {
        super(props);
        this.state = {
            params: {
                value: localStorage.getItem(SEARCH_PARAMETERS) ? localStorage.getItem(SEARCH_PARAMETERS) : '',
                isValid: false
            }
        };
    }

    handleInputChange = (event) => {
        const target = event.target;
        const inputValue = target.value;
        if (inputValue.match(SEARCH_REGEX_PATTERN)) {
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
            localStorage.setItem(SEARCH_PARAMETERS, this.state.params.value);
            Alert.info(getMessage(this.props, 'notReadableSearch'))
        } else {
            let search = this.state.params.value;
            search = search.replace(/(?<!\u0028)\b[\w\s]+\b(?![\w\s]*[\u0029])/,
                function (match) {
                    return `&name=l:${match}&description=l:${match}`
                });
            if (search.includes('#(')) {
                search = search.replace('#(', '&tag_name=').replace(')', '');
                while (search.includes('#(')) {
                    search = search.replace(/(#\u0028\w+( ?)\w+\u0029)/,
                        function (match, p1) {
                            p1 = p1.replace('#', '').replace('(', '').replace(')', '');
                            return `,${p1}`
                        })
                }
            }
            search = search.replace(/\$\u0028([<>]?)\d+\u002E?\d?\d?(.\d+\u002E?\d?\d?)?\u0029/,
                function (match) {
                    match = match.replace('$(', '').replace(')', '');
                    if (search.includes('>')) {
                        match = match.replace('>', '');
                        let max = Number.MAX_SAFE_INTEGER.toString();
                        return `&price=bw:${match},${max}`;
                    } else if (search.includes('<')) {
                        match = match.replace('<', '');
                        return `&price=bw:0,${match}`
                    } else if (search.includes('-')) {
                        let array = match.split('-');
                        return `&price=bw:${array[0]},${array[1]}`
                    } else {
                        return `&price=${match}`
                    }
                });
            search = search.replace(/\s/g, '');
            let href = CERTIFICATES_DEFAULT_REQUEST_URL + search + '&page=1&size=' + this.props.size;
            localStorage.setItem(SEARCH_PARAMETERS, this.state.params.value);
            this.props.pageHandler(href);
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
                        style={pStyle}
                    />
                    <AvFeedback>{getMessage(this.props, 'searchViolation')}</AvFeedback>
                    <Button outline color="secondary" >{getMessage(this.props, 'searchCommand')}</Button>
                </AvGroup>
            </AvForm>

    }

}

export default withCookies(Search);
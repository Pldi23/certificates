import React from "react";
import {Button, Col, Label} from "reactstrap";
import {withCookies} from "react-cookie";
import {AvFeedback, AvForm, AvGroup, AvInput} from "availity-reactstrap-validation";
import {getMessage} from "../../app/Message";
import {CERTIFICATES_DEFAULT_REQUEST_URL} from "../../constants";

const pStyle = {
    width: '435px',
    marginRight: '10px',
};

class Search extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            params: ''
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
        console.log(inputValue)
    }

    handleSubmit() {

        let search = this.state.params;
        search = search.replace(/(?<!\u0028)\b[\w\s]+\b(?![\w\s]*[\u0029])/,
            function (match) {
                return `&name=l:${match}&description=l:${match}`
            });
        console.log(search);
        if (search.includes('#(')) {
            search = search.replace('#(', '&tag_name=').replace(')', '');
            while (search.includes('#(')) {
                search = search.replace(/(#\u0028\w+( ?)\w+\u0029)/,
                    function (match, p1) {
                        p1 = p1.replace('#', '').replace('(', '').replace(')', '');
                        return `,${p1}`
                    })
            }
            console.log(search);

        }
        search = search.replace(/\$\u0028([<>]?)\d+\u002E?\d?\d?(.\d+\u002E?\d?\d?)?\u0029/,
            function (match) {
                console.log(match)
                match = match.replace('$(', '').replace(')', '');
                console.log(match)
                if (search.includes('>')) {
                    match = match.replace('>', '');
                    let max = Number.MAX_SAFE_INTEGER.toString();
                    return `&price=bw:${match},${max}`;
                }
                if (search.includes('<')) {
                    match = match.replace('<', '');
                    return `&price=bw:0,${match}`
                }
                if (search.includes('-')) {
                    let array = match.split('-');
                    console.log(array);
                    console.log(match);
                    return `&price=bw:${array[0]},${array[1]}`
                }
            });
        search = search.replace(/\s/g, '');
        let href = CERTIFICATES_DEFAULT_REQUEST_URL + search + '&page=1&size=' + this.props.size;

        console.log(href)
        this.props.pageHandler(href);
    }

    render() {

        return <AvForm inline onSubmit={this.handleSubmit}>
            <Col sm="12" md={{size: 9, offset: 3}}>
                <AvGroup>
                    <Label for="search" hidden>Search</Label>
                    <AvInput
                        type="text"
                        name="params"
                        id="search"
                        placeholder={getMessage(this.props, 'searchParams')}
                        required
                        pattern={"([\\w ]+)|(\\#\u0028\\w+( ?)\\w+\u0029)|(\\$\u0028([<>]?)\\d+\u002E?\\d?\\d?(.\\d+\u002E?\\d?\\d?)?\u0029)"}
                        maxLength="100"
                        minLength="1"
                        value={this.state.params}
                        onChange={this.handleInputChange}
                        style={
                            pStyle
                        }
                    />
                    <AvFeedback>{getMessage(this.props, 'searchViolation')}</AvFeedback>
                    <Button outline color="secondary">{getMessage(this.props, 'searchCommand')}</Button>
                </AvGroup>
            </Col>
        </AvForm>
    }

}

export default withCookies(Search);
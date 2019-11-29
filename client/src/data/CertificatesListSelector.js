import React from 'react';
import Select from 'react-select';
import {withCookies} from "react-cookie";
import {SEARCH_PARAMETERS} from "../constants";
import * as PropTypes from "prop-types";
import {getMessage, getMessageByLocale} from "../app/Message";


const colourStyles = {
    control: styles => ({
        ...styles,
        width: '150px',
        right: '5px',
        top: '4px'
    })

};

class CertificatesListSelector extends React.Component {

    static propTypes = {
        selected: PropTypes.bool.isRequired,
        reloadHandler: PropTypes.func.isRequired,
        ordersHandler: PropTypes.func.isRequired,
        locale: PropTypes.string.isRequired
    };

    constructor(props) {
        super(props);
        this.state = {
            selectedOption: null,
        };
    }

    componentDidMount() {
        this.setState({
            selectedOption: {
                value: !this.props.selected ? 'All' : 'My Certificates',
                label: !this.props.selected ? getMessageByLocale(this.props.locale, 'all') : getMessageByLocale(this.props.locale, 'myCertificates')
            }
        })
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevProps.locale !== this.props.locale) {
            this.setState({
                selectedOption: {
                    value: !this.props.selected ? 'All' : 'My Certificates',
                    label: !this.props.selected ? getMessageByLocale(this.props.locale, 'all') : getMessageByLocale(this.props.locale, 'myCertificates')
                }
            })
        }
    }

    handleChange = selectedOption => {
        this.setState({selectedOption});

        if (selectedOption.value === 'All') {
            this.props.reloadHandler()
        } else {
            localStorage.removeItem(SEARCH_PARAMETERS);
            this.props.ordersHandler()
        }
    };

    render() {
        let {selectedOption} = this.state;
        let options = [
            {value: 'All', label: getMessage(this.props, 'all')},
            {value: 'My Certificates', label: getMessage(this.props, 'myCertificates')},
        ];

        return (
            <Select styles={colourStyles}
                    value={selectedOption}
                    onChange={this.handleChange}
                    options={options}
            />
        );
    }
}


export default withCookies(CertificatesListSelector)
import React from 'react';
import Select from 'react-select';
import {withCookies} from "react-cookie";
import {SEARCH_PARAMETERS} from "../constants";
import * as PropTypes from "prop-types";
import {getMessage} from "../app/Message";



// const options = [
//     {value: 'All', label: 'All'},
//     {value: 'My Certificates', label: 'My Certificates'},
// ];

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
    };

    constructor(props) {
        super(props);
        this.state = {
            selectedOption: {
                value: !this.props.selected ? 'All' : 'My Certificates',
                label: !this.props.selected ? getMessage(this.props, 'all') : getMessage(this.props, 'myCertificates')
            },
        };
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
        const {selectedOption} = this.state;
        const options = [
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
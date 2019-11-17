import React from 'react';
import Select from 'react-select';
import {withCookies} from "react-cookie";

const options = [
    { value: 'All', label: 'All' },
    { value: 'Reviewed', label: 'Reviewed' },
];

const colourStyles = {
    control: styles => ({
        ...styles,
        width: '100px'})

};

class CertificatesListSelector extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            selectedOption: {
                value: this.props.size,
                label: this.props.size
            },
        };

    }

    handleChange = selectedOption => {
        this.setState({ selectedOption });
        this.props.listHandler(selectedOption.value)
    };

    render() {
        const { selectedOption } = this.state;

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
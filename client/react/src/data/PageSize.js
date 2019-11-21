import React from 'react';
import Select from 'react-select';
import {withCookies} from "react-cookie";
import * as PropTypes from "prop-types";

const options = [
    { value: '5', label: '5' },
    { value: '25', label: '25' },
    { value: '50', label: '50' },
    { value: '100', label: '100' },
];

const colourStyles = {
    control: styles => ({
        ...styles,
        width: '100px'})

};

class PageSize extends React.Component {

    static propTypes = {
        size: PropTypes.string.isRequired,
        pageSizeHandler: PropTypes.func.isRequired
    };

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
        this.props.pageSizeHandler(selectedOption.value)
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


export default withCookies(PageSize)
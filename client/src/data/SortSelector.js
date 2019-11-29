import React from "react";
import {withCookies} from "react-cookie";
import {Select} from "react-select";

const options = [
    {value: 'creationdate', label: 'creationdate'},
    {value: 'expirationdate', label: 'expirationdate'},
    {value: 'price', label: 'price'},
];

const colourStyles = {
    control: styles => ({
        ...styles,
        width: '150px',
        right: '5px',
        top: '4px'
    })

};

class SortSelector extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            selectedOption: {
                value: this.props.selected,
                label: this.props.selected
            },
        };
    }

    render() {
        const {selectedOption} = this.state;

        return (
            <Select styles={colourStyles}
                    value={selectedOption}
                    // onChange={this.handleChange}
                    options={options}
            />
        );
    }
}


export default withCookies(SortSelector)
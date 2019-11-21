import React from "react";
import {withCookies} from "react-cookie";
import * as PropTypes from "prop-types";


class Tag extends React.Component {

    static propTypes = {
        key: PropTypes.string,
        tag: PropTypes.object.isRequired,
        tagSearchHandler: PropTypes.func.isRequired
    };


    render() {
        return <button className={'badge badge-info'} onClick={() => this.props.tagSearchHandler(this.props.tag.tag.title)}>
            {this.props.tag.tag.title}
        </button>
    }
}

export default withCookies(Tag)
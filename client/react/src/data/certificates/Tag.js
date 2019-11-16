import React from "react";
import {withCookies} from "react-cookie";


class Tag extends React.Component {


    render() {
        return <button className={'badge badge-info'} onClick={() => this.props.tagSearchHandler(this.props.tag.tag.title)}>
            {this.props.tag.tag.title}
        </button>
    }
}

export default withCookies(Tag)
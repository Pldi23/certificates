import React from "react";
import {Link} from "react-router-dom";
import {withCookies} from "react-cookie";
import {MdModeEdit} from "react-icons/md";

const pStyle = {
    fontSize: '15px',
    textAlign: 'center',
    paddingTop: '9px',
    marginLeft: '3px',
    marginRight: '3px',
};

class EditLink extends React.Component {

    render() {
        return this.props.link ?
            <Link to={{
                pathname: '/edit',
                state: {
                    name: {
                        value: this.props.name,
                        isValid: true
                    },
                    description: {
                        value: this.props.description,
                        isValid: true
                    },
                    price: {
                        value: this.props.price,
                        isValid: true
                    },
                    expiration: {
                        value: this.props.expiration,
                        isValid: true
                    },
                    tags: this.props.tags,
                    link: this.props.link,
                    isBlocking: true
                }
            }}
                  className="btn btn-danger btn-sm" style={pStyle}>
                <span><MdModeEdit /></span>
            </Link>
            : null;

    }
}

export default withCookies(EditLink)
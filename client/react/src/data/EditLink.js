import React from "react";
import {Link} from "react-router-dom";
import {withCookies} from "react-cookie";
import {MdModeEdit} from "react-icons/md";
import * as PropTypes from "prop-types";
import './Certificates.css'

class EditLink extends React.Component {

    static propTypes = {
        locale: PropTypes.string.isRequired,
        link: PropTypes.object,
        name: PropTypes.string.isRequired,
        description: PropTypes.string.isRequired,
        price: PropTypes.number.isRequired,
        expiration: PropTypes.string.isRequired,
        tags: PropTypes.array.isRequired,

    };

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
                  className="btn btn-danger btn-sm editStyle">
                <span><MdModeEdit /></span>
            </Link>
            : null;

    }
}

export default withCookies(EditLink)
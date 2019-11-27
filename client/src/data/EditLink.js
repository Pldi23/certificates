import React from "react";
import {withCookies} from "react-cookie";
import {MdModeEdit} from "react-icons/md";
import * as PropTypes from "prop-types";
import './Certificates.css'
import Button from "reactstrap/es/Button";
import {withRouter} from "react-router-dom";
import {getCertificateById} from "../service/APIService";
import {getMessage} from "../app/Message";
import Alert from "react-s-alert";

class EditLink extends React.Component {

    static propTypes = {
        locale: PropTypes.string.isRequired,
        link: PropTypes.object,
        name: PropTypes.string.isRequired,
        description: PropTypes.string.isRequired,
        price: PropTypes.number.isRequired,
        expiration: PropTypes.string.isRequired,
        tags: PropTypes.array.isRequired,
        id: PropTypes.number.isRequired,
        reloadHandler: PropTypes.func.isRequired,
        availabilityChecker: PropTypes.func.isRequired
    };

    handleClick = () => {
        getCertificateById(this.props.id, this.props).then(response => {
            if (!response.ok) {
                Alert.error(getMessage(this.props, 'certificateUnavailable'));
                this.props.reloadHandler();
            } else {
                this.props.history.push({
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
                        isBlocking: true,
                        id: this.props.id
                    }
                });
            }
        })
    };

    render() {
        return this.props.link ?
            <Button className={'editStyle btn btn-danger btn-sm'}
                    onClick={() => this.handleClick()}>
                <span><MdModeEdit/></span>
            </Button>
            : null;

    }
}

export default withRouter(withCookies(EditLink))
import React from "react";
import {deleteCertificate, getCertificateById} from "../service/APIService";
import Alert from "react-s-alert";
import {Button} from "reactstrap";
import confirm from "reactstrap-confirm";
import {withCookies} from "react-cookie";
import {getMessage} from "../app/Message";
import {MdDelete} from "react-icons/md";
import * as PropTypes from "prop-types";
import './Certificates.css'

class DeleteLink extends React.Component {

    static propTypes = {
        link: PropTypes.object,
        reloadHandler: PropTypes.func.isRequired,
        availabilityChecker: PropTypes.func.isRequired,
        id: PropTypes.number.isRequired,
    };

    deleteHandler = () => {
        getCertificateById(this.props.id, this.props).then(response => {
            if (!response.ok) {
                Alert.error(getMessage(this.props, 'certificateUnavailable'));
                this.props.reloadHandler();
            } else {
                deleteCertificate(this.props, this.props.link.href).then(response => {
                    if (response.ok) {
                        Alert.success(getMessage(this.props, 'certificateDeleted'));
                        this.props.reloadHandler();
                    } else {
                        response.json().then(json => {
                            let message = JSON.stringify(json.messages);
                            Alert.error(message.substring(1, message.length - 1));
                        });
                    }
                }).catch(error => {
                    Alert.error((error && error.message) || getMessage(this.props, 'error'));
                })
            }
        })
    };


    render() {
        return this.props.link ? <Button disabled={this.props.disable} onClick={
            async () => {
                let result = await confirm({
                    title: '',
                    message: getMessage(this.props, 'areYouSure'),
                    confirmText: getMessage(this.props, 'ok'),
                    cancelText: getMessage(this.props, 'cancel'),
                    confirmColor: "link text-danger",
                    cancelColor: "primary"
                });
                return result ? this.deleteHandler() : null
            }
        } className="btn btn-danger btn-sm deleteStyle">
                <span><MdDelete /></span>
        </Button>
            : null;
    }
}

export default withCookies(DeleteLink)
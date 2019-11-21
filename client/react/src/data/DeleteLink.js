import React from "react";
import {deleteCertificate} from "../service/APIService";
import Alert from "react-s-alert";
import {Button} from "reactstrap";
import confirm from "reactstrap-confirm";
import {withCookies} from "react-cookie";
import {getMessage} from "../app/Message";
import {MdDelete} from "react-icons/md";
import * as PropTypes from "prop-types";

const pStyle = {
    marginLeft: '3px',
    marginRight: '3px',
};

class DeleteLink extends React.Component {

    static propTypes = {
        link: PropTypes.object.isRequired,
        reloadHandler: PropTypes.func.isRequired,
    };

    deleteHandler = () => {
        deleteCertificate(this.props, this.props.link.href).then(response => {
            if (response.ok) {
                this.props.reloadHandler();
                Alert.success(getMessage(this.props, 'certificateDeleted'));
            } else {
                response.json().then(json => {
                    let message = JSON.stringify(json.messages);
                    Alert.error(message.substring(1, message.length - 1));
                });
            }
        }).catch(error => {
            Alert.error((error && error.message) || getMessage(this.props, 'error'));
        });
    };


    render() {
        return this.props.link ? <Button disabled={this.props.disable} style={pStyle} onClick={
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
        } className="btn btn-danger btn-sm">
                <span><MdDelete /></span>
        </Button>
            : null;
    }
}

export default withCookies(DeleteLink)
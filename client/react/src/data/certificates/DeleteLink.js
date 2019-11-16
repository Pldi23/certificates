import React from "react";
import {deleteCertificate} from "../../util/APIUtils";
import Alert from "react-s-alert";
import {Button} from "reactstrap";
import confirm from "reactstrap-confirm";
import {withCookies} from "react-cookie";
import {getMessage} from "../../app/Message";

class DeleteLink extends React.Component {

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
        } className="btn btn-danger btn-sm">
            {getMessage(this.props, 'delete')}
        </Button>
            : null;
    }
}

export default withCookies(DeleteLink)
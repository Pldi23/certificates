import React from "react";
import {Button} from "reactstrap";
import {getMessage, getMessageByLocale} from "../../app/Message";
import {withCookies} from "react-cookie";
import {ACCESS_TOKEN} from "../../constants";
import confirm from "reactstrap-confirm";
import {Link} from "react-router-dom";

class BuyLink extends React.Component {



    clickHandler = () => {
        if (localStorage.getItem(ACCESS_TOKEN)) {
            // this.props.buyHandler(this.props.id)
        } else {
            return async () => {
                let result = await confirm({
                    title: '',
                    // message: getMessage(this.props, 'areYouSure'),
                    // confirmText: getMessage(this.props, 'ok'),
                    // cancelText: getMessage(this.props, 'cancel'),
                    message: 'please login',
                    confirmText: 'login',
                    cancelText: 'later',
                    confirmColor: "link text-danger",
                    cancelColor: "primary"
                });
                return result ? this.props.history.push('/login') : null
            }
            // modal ? go to login ; cancel
        }
    }



    render() {
        return this.props.link ?
            <Button onClick={() => this.clickHandler(this.props.id)} color="danger" size={"sm"}>{getMessageByLocale(this.props.locale, 'buy')}</Button> : null;
    }
}

export default withCookies(BuyLink)
import React from "react";
import {Link} from "react-router-dom";
import {getMessageByLocale} from "../../app/Message";
import {withCookies} from "react-cookie";

const pStyle = {
    fontSize: '15px',
    textAlign: 'center',
    paddingTop: '9px'
};

class EditLink extends React.Component {

    render() {
        return this.props.link ?
            <Link to={{
                pathname: '/edit',
                state: {
                    name: this.props.name,
                    description: this.props.description,
                    price: this.props.price,
                    expiration: this.props.expiration,
                    tags: this.props.tags,
                    link: this.props.link,
                    isBlocking: true
                }
            }}
                  className="btn btn-danger btn-sm" style={pStyle}
            >
                {getMessageByLocale(this.props.locale, 'edit')}
            </Link>
            : null;

    }
}

export default withCookies(EditLink)
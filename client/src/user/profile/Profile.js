import React, {Component} from 'react';
import {Container, Col, Alert} from 'reactstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import {getMessageByLocale} from "../../app/Message";
import {ROLE_ADMIN} from "../../constants";
import * as PropTypes from "prop-types";

class Profile extends Component {

    static propTypes = {
        locale: PropTypes.string.isRequired
    };

    constructor(props) {
        super(props);
        this.state = {
            user: this.props.currentUser
        };
    }

    componentDidMount() {
        this.props.routeHandler(false);
    }

    render() {
        return <div>
            <Container>
                <Col>

                </Col>
                <Col>
                    <Alert color="success">
                    <span>{this.state.user.user.email}</span>
                        <hr/>
                    <b>{this.state.user.user.role === (ROLE_ADMIN) ? getMessageByLocale(this.props.locale, 'admin') : null}</b>
                    </Alert>
                </Col>

            </Container>
        </div>
    }
}

export default Profile
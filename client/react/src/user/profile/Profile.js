import React, {Component} from 'react';
import './Profile.css';
import {Container, Col} from 'reactstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

class Profile extends Component {
    constructor(props) {
        super(props);
        this.state = {
            user: this.props.currentUser
        };
        console.log(this.state.user);
    }

    render() {
        return <div>
            <Container>
                <Col>

                </Col>
                <Col>
                    <span> {this.state.user.user.id}, {this.state.user.user.email}, {this.state.user.user.role}</span>
                </Col>

            </Container>
        </div>
    }
}

export default Profile
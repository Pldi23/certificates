import React, {Component} from "react";
import {Container, Navbar, Col} from "reactstrap";
import LocalizedStrings from 'react-localization';
import { message } from '../app/Message'
import * as PropTypes from "prop-types";

class AppFooter extends Component {

    static propTypes = {
        locale: PropTypes.string.isRequired,
    };

    render() {
        let strings = new LocalizedStrings({data: message});
        strings.setContent(message);
        strings.setLanguage(this.props.locale);
        return <div className="fixed-bottom">
            <Navbar color="primary" dark>
                <Container className="display-6 text-center">
                    <Col sm="12" md={{ size: 6, offset: 3 }}>
                        <span>{strings.footer}</span>
                    </Col>
                </Container>
            </Navbar>
        </div>
    }
}

export default AppFooter;
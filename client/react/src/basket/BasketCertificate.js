import React from "react";
import {withCookies} from "react-cookie";
import {MdClear} from "react-icons/md";
import CartCertificateModal from "./CartCertificateModal";
import {Badge, Col, Row} from "reactstrap";
import * as PropTypes from "prop-types";
import './Basket.css'

class BasketCertificate extends React.Component {

    static propTypes = {
        certificate: PropTypes.object.isRequired,
        onRemoveFromBasket: PropTypes.func.isRequired,
    };

    render() {
        return <div>
            <Row className={'rowStyle'}>
                <Col>
                    <CartCertificateModal certificate={this.props.certificate}/>
                </Col>
                <Col>
                    <Badge>
                        {this.props.certificate.price}$
                    </Badge>
                </Col>
                <Col>
                    <button className={'minusStyle'}
                            onClick={() => this.props.onRemoveFromBasket(this.props.certificate)}>
                        <span><MdClear/></span>
                    </button>
                </Col>

            </Row>
        </div>
    }
}

export default withCookies(BasketCertificate)
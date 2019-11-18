import React from "react";
import {withCookies} from "react-cookie";
// import Badge from "reactstrap/es/Badge";
// import Row from "reactstrap/es/Row";
// import Col from "reactstrap/es/Col";
import {MdClear} from "react-icons/md";
import CartCertificateModal from "./CartCertificateModal";
import {Badge, Col, Row} from "reactstrap";


const minusStyle = {

    backgroundColor: '#eee',
    borderTopWidth: '0px',
    borderLeftWidth: '0px',
    borderBottomWidth: '0px',
    borderRightWidth: '0px',
};

const rowStyle = {
    paddingBottom: '5px',
    borderBottomColor: '#6c757d'
};

class BasketCertificate extends React.Component {


    render() {
        return <div>
            <Row style={rowStyle}>
                <Col>
                    <CartCertificateModal certificate={this.props.certificate}/>
                </Col>
                <Col>
                    <Badge>
                        {this.props.certificate.price}$
                    </Badge>
                </Col>
                <Col>
                    <button style={minusStyle}
                            onClick={() => this.props.onRemoveFromBasket(this.props.certificate)}>
                        <span><MdClear/></span>
                    </button>
                </Col>

            </Row>
        </div>
    }
}

export default withCookies(BasketCertificate)
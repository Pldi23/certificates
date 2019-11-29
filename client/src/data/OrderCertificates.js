import React from "react";
import * as PropTypes from "prop-types";
import Certificate from "./Certificate";
import {getMessage} from "../app/Message";
import {withCookies} from "react-cookie";
import {CardColumns, Col} from "reactstrap";

class OrderCertificates extends React.Component {

    static propTypes = {
        certificates: PropTypes.array.isRequired,
        locale: PropTypes.string.isRequired,
        created: PropTypes.string.isRequired,
        reloadHandler: PropTypes.func.isRequired,
        tagSearchHandler: PropTypes.func.isRequired,
        onAddToBasket: PropTypes.func.isRequired,
        showButtons: PropTypes.bool.isRequired,

    };


    getParsedDate(date) {
        date = String(date).split('-');
        let year = String(date[0]);
        let month = String(date[1]);
        let day = String(date[2]).split('T');
        return day[0] + '/' + month + '/' + year
    }

    render() {

        const certificates = this.props.certificates.map((certificate, index) =>
            <Certificate
                key={index}
                certificate={certificate}
                locale={this.props.locale}
                reloadHandler={this.props.reloadHandler}
                tagSearchHandler={this.props.tagSearchHandler}
                tagSearchPlusHandler={this.props.tagSearchPlusHandler}
                onAddToBasket={this.props.onAddToBasket}
                showButtons={false}
                priceGt={this.props.priceGt}
            />
        );

        return <div>
            <hr/>
            <Col>
                <p className="text-info">{getMessage(this.props, 'orderDate')}{this.getParsedDate(this.props.created)}</p>
            </Col>
            <Col>
                <p className="text-info">{getMessage(this.props, 'totalPrice')}{this.props.price}</p>
            </Col>
            <CardColumns>
                {certificates}
            </CardColumns>

        </div>

    }

}

export default withCookies(OrderCertificates)
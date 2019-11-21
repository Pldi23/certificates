import React from 'react';
import {withCookies} from 'react-cookie';
import Certificate from "./Certificate";
import {CardColumns, Col} from "reactstrap";
import * as PropTypes from "prop-types";

class OrdersCertificatesList extends React.Component {

    static propTypes = {
        orders: PropTypes.array.isRequired,
        locale: PropTypes.string.isRequired,
        reloadHandler: PropTypes.func.isRequired,
        tagSearchHandler: PropTypes.func.isRequired,
        onAddToBasket: PropTypes.func.isRequired,

    };

    render() {

        const orders = this.props.orders.map((item) =>

            <OrderCertificates
                key={item.order.id}
                certificates={item.certificates}
                locale={this.props.locale}
                created={item.order.createdAt}
                price={item.order.price}
                reloadHandler={this.props.reloadHandler}
                tagSearchHandler={this.props.tagSearchHandler}
                onAddToBasket={this.props.onAddToBasket}
                showButtons={false}
            />);


        return <div>{orders}</div>
    }
}

export default withCookies(OrdersCertificatesList)

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
                onAddToBasket={this.props.onAddToBasket}
                showButtons={false}
            />
        );

        return <div>
            <hr/>
            <Col>
                <p className="text-info">Order date: {this.getParsedDate(this.props.created)}</p>
            </Col>
            <Col>
                <p className="text-info">Total price: {this.props.price}</p>
            </Col>
            <CardColumns>
                {certificates}
            </CardColumns>

        </div>

    }

}
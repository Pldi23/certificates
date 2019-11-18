import React from 'react';
import {withCookies} from 'react-cookie';
import Certificate from "./Certificate";
import {CardColumns, Col} from "reactstrap";

class OrdersCertificatesList extends React.Component {

    render() {

        const orders = this.props.orders.map((item, index) =>

            <OrderCertificates
                key={index}
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
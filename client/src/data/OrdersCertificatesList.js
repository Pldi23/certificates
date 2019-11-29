import React from 'react';
import {withCookies} from 'react-cookie';
import * as PropTypes from "prop-types";
import OrderCertificates from "./OrderCertificates";

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
                tagSearchPlusHandler={this.props.tagSearchPlusHandler}
                onAddToBasket={this.props.onAddToBasket}
                showButtons={false}
                priceGt={this.props.priceGt}
            />);


        return <div>{orders}</div>
    }
}

export default withCookies(OrdersCertificatesList)
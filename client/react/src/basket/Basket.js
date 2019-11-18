import React from 'react';
import {PopoverBody, PopoverHeader, UncontrolledPopover} from "reactstrap";
import BasketCertificate from "./BasketCertificate";
import {getMessage} from "../app/Message";
import {withCookies} from "react-cookie";
import {FaCartArrowDown} from "react-icons/fa";
import ConfirmPurchaseModal from "./ConfirmPurchaseModal";

const cartStyle = {

    backgroundColor: '#eee',
    borderTopWidth: '0px',
    borderLeftWidth: '0px',
    borderBottomWidth: '0px',
    borderRightWidth: '0px',
};

class Basket extends React.Component {



    render() {
        const certificates = this.props.basketCertificates.map((giftCertificate, index) =>

            <BasketCertificate
                key={giftCertificate.id + index}
                certificate={giftCertificate}
                onRemoveFromBasket={this.props.onRemoveFromBasket}
            />
        );
        return <div>
            <button style={cartStyle} id="PopoverLegacy" type="button">
                <span><FaCartArrowDown/></span>
            </button>
            <UncontrolledPopover trigger="legacy" placement="bottom" target="PopoverLegacy">
                <PopoverHeader>{getMessage(this.props, 'cart')}</PopoverHeader>
                <PopoverBody>
                    {certificates}
                    {certificates.length > 0 ? (
                        <div>
                            <ConfirmPurchaseModal
                                onRefreshBasket={this.props.onRefreshBasket}
                                basketCertificates={this.props.basketCertificates}
                            />
                        </div>
                    ) : (
                        <b>{getMessage(this.props, 'emptyCart')}</b>
                    )}
                </PopoverBody>
            </UncontrolledPopover>
        </div>
    }

}

export default withCookies(Basket);
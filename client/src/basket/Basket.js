import React from 'react';
import {PopoverBody, PopoverHeader, UncontrolledPopover} from "reactstrap";
import BasketCertificate from "./BasketCertificate";
import {getMessage} from "../app/Message";
import {withCookies} from "react-cookie";
import {FaCartArrowDown} from "react-icons/fa";
import ConfirmPurchaseModal from "./ConfirmPurchaseModal";
import * as PropTypes from "prop-types";
import './Basket.css'

class Basket extends React.Component {

    static propTypes = {
        basketCertificates: PropTypes.array.isRequired,
        onRemoveFromBasket: PropTypes.func.isRequired,
        onRefreshBasket: PropTypes.func.isRequired,
    };

    render() {
        const certificates = this.props.basketCertificates.map((giftCertificate, index) =>

            <BasketCertificate
                key={index}
                certificate={giftCertificate}
                onRemoveFromBasket={this.props.onRemoveFromBasket}
            />
        );
        const quantity = certificates.length || null;
        return <div>
            <button className={'button'}  id="PopoverLegacy" type="button">
                <FaCartArrowDown/>{quantity}
            </button>
            <UncontrolledPopover trigger="legacy" placement="bottom" target="PopoverLegacy">
                <PopoverHeader>{getMessage(this.props, 'cart')}</PopoverHeader>
                <PopoverBody>
                    {certificates}
                    {certificates.length ? (
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
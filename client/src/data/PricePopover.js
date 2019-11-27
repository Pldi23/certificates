import React, {useState} from "react";
import {Button, Fade} from "reactstrap";
import {withCookies} from "react-cookie";
import './Certificates.css'

const PricePopover = (props) => {
    const [fadeIn, setFadeIn] = useState(false);

    const toggle = () => setFadeIn(!fadeIn);

    return (
        <div>
            <Button className={'badge badge-success'} onClick={toggle}>{props.amount}</Button>
            <Fade in={fadeIn} tag="h5" className="mt-3">
               <button className={'btnPopover'} onClick={() => props.priceGt(props.amount, 'gt')}>&#62;</button>
                <button className={'btnPopover'} onClick={() => props.priceGt(props.amount, 'is')}>&#61;</button>
                 <button className={'btnPopover'} onClick={() => props.priceGt(props.amount, 'lw')}>&#60;</button>
            </Fade>
        </div>
    );
};

export default withCookies(PricePopover);
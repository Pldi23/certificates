import React from "react";
import Certificate from "./Certificate";
import {CardColumns} from "reactstrap";
import {withCookies} from "react-cookie";
import * as PropTypes from "prop-types";

class CertificatesList extends React.Component {

    static propTypes = {
        certificates: PropTypes.array.isRequired,
        locale: PropTypes.string.isRequired,
        reloadHandler: PropTypes.func.isRequired,
        tagSearchHandler: PropTypes.func.isRequired,
        onAddToBasket: PropTypes.func.isRequired,

    };

    constructor(props) {
        super(props);
        this.state = {
            certificates: this.props.certificates
        }
    }

    render() {
        const certificates = this.state.certificates.map((giftCertificate, index) =>

            <Certificate
                key={index}
                certificate={giftCertificate}
                locale={this.props.locale}
                reloadHandler={this.props.reloadHandler}
                tagSearchHandler={this.props.tagSearchHandler}
                tagSearchPlusHandler={this.props.tagSearchPlusHandler}
                onAddToBasket={this.props.onAddToBasket}
                showButtons={true}
                priceGt={this.props.priceGt}
                availabilityChecker={this.props.availabilityChecker}
            />
        );
        return (
            <CardColumns>
                {certificates}
            </CardColumns>
        )
    }
}

export default withCookies(CertificatesList);
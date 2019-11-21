import React from "react";
import Tag from "./Tag";
import {Badge, ButtonGroup, Card, CardBody, CardFooter, CardHeader, CardText, Col, Row} from "reactstrap";
import CertificateModal from "./CertificateModal";
import {getMessageByLocale} from "../app/Message";
import EditLink from "./EditLink";
import DeleteLink from "./DeleteLink";
import {withCookies} from "react-cookie";
import BuyButtonNotAuthorizedModal from "./BuyButtonNotAuthorizedModal";
import BuyButtonModal from "./BuyButtonModal";
import {ACCESS_TOKEN} from "../constants";
import * as PropTypes from "prop-types";


class Certificate extends React.Component {

    static propTypes = {
        key: PropTypes.number,
        certificate: PropTypes.object.isRequired,
        locale: PropTypes.string.isRequired,
        reloadHandler: PropTypes.func.isRequired,
        tagSearchHandler: PropTypes.func.isRequired,
        onAddToBasket: PropTypes.func.isRequired,
        showButtons: PropTypes.bool.isRequired,

    };



    getParsedDate(date) {
        date = String(date).split('/');
        let days = String(date[0]);
        let months = String(date[1]);
        let years = String(date[2]);
        return years + '-' + months + '-' + days;
    }

    render() {

        let date = this.getParsedDate(this.props.certificate.giftCertificate.expirationDate);
        const tags = this.props.certificate.tags.map((tag, index) =>
            <Tag
                key={index}
                tag={tag}
                tagSearchHandler={this.props.tagSearchHandler}
            />
        );
        const editLink = this.props.certificate._links.update;
        const deleteLink = this.props.certificate._links.delete;
        const buyLink = this.props.certificate._links.self;
        const description = this.props.certificate.giftCertificate.description;
        return (
            <Card body className="text-center">
                <CardHeader>
                    <Row>
                        <CertificateModal
                            certificate={this.props.certificate.giftCertificate}
                            locale={this.props.locale}
                            tags={tags}/>
                        <Col className="text-right">
                            {this.props.certificate.giftCertificate.creationDate}
                        </Col>
                    </Row>

                </CardHeader>
                <CardBody>
                    <CardText>{description.length < 250 ? description : description.substring(0, 246).concat(' ...')}</CardText>
                    <CardText>
                        {getMessageByLocale(this.props.locale, 'expiring')}
                        {this.props.certificate.giftCertificate.expirationDate}</CardText>
                    <CardText>
                        {getMessageByLocale(this.props.locale, 'tags')}
                        {tags}</CardText>
                </CardBody>
                <CardFooter>
                    {this.props.showButtons ? (

                    <ButtonGroup size="sm">
                            <EditLink
                                locale={this.props.locale}
                                link={editLink}
                                name={this.props.certificate.giftCertificate.name}
                                description={this.props.certificate.giftCertificate.description}
                                price={this.props.certificate.giftCertificate.price}
                                expiration={date}
                                tags={this.props.certificate.tags.map(tag => {
                                    return {
                                        id: tag.tag.title,
                                        text: tag.tag.title
                                    }
                                })}/>{' '}
                            <DeleteLink
                                reloadHandler={this.props.reloadHandler}
                                link={deleteLink}
                                props={this.props}/>
                            {localStorage.getItem(ACCESS_TOKEN) ? (
                                <BuyButtonModal
                                    link={buyLink}
                                    certificate={this.props.certificate.giftCertificate}
                                    onAddToBasket={this.props.onAddToBasket}
                                    locale={this.props.locale}/>
                            ) : (
                                <BuyButtonNotAuthorizedModal
                                    locale={this.props.locale}
                                />
                            )}
                    </ButtonGroup>
                    ) : (null)}
                            <Badge color="success">
                                {this.props.certificate.giftCertificate.price}$
                            </Badge>
                </CardFooter>
            </Card>
        )
    }
}

export default withCookies(Certificate)
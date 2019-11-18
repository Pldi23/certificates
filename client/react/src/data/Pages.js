import * as React from "react";
import {Pagination, PaginationItem, PaginationLink} from "reactstrap";
import {withCookies} from "react-cookie";
import {getMessageByLocale} from "../app/Message";

class Pages extends React.Component {

    render() {
        const pages = this.props.links.pages;
        if (pages.length > 1) {
                return <Pagination aria-label="Page navigation">
                    {pages.map(p =>
                        <PageItem
                            key={p.href}
                            link={p}
                            pageHandler={this.props.pageHandler}
                            locale={this.props.cookies.cookies.locale}
                        />)}
                </Pagination>
        }
        return null
    }
}

class PageItem extends React.Component {
    render() {
        const link = this.props.link;
        return <PaginationItem active={link.current}>
            <PaginationLink onClick={ () =>
                this.props.pageHandler(link.href)}>
                {link.name.includes('first page') ? getMessageByLocale(this.props.locale, 'firstPage') :
                    link.name.includes('last page') ? getMessageByLocale(this.props.locale, 'lastPage') : link.name}
            </PaginationLink>
        </PaginationItem>
    }

}

export default withCookies(Pages)
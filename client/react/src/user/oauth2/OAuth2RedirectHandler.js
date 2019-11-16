import React, { Component } from 'react';
import {ACCESS_TOKEN, ACCESS_TOKEN_EXPIRES_IN, PREV_PATH, REFRESH_TOKEN} from '../../constants';
import { Redirect } from 'react-router-dom'

class OAuth2RedirectHandler extends Component {
    getUrlParameter(name) {
        name = name.replace(/[[]/, '[').replace(/[\]]/, '\\]');
        let regex = new RegExp('[\\?&]' + name + '=([^&#]*)');

        let results = regex.exec(this.props.location.search);
        return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
    };

    render() {        
        const token = this.getUrlParameter('token');
        const error = this.getUrlParameter('error');
        const refreshToken = this.getUrlParameter('refresh');
        const expiresIn = this.getUrlParameter('expiresIn');

        if(token) {
            localStorage.setItem(ACCESS_TOKEN, token);
            if (refreshToken) {
                localStorage.setItem(REFRESH_TOKEN, refreshToken)
            }
            if (expiresIn) {
                localStorage.setItem(ACCESS_TOKEN_EXPIRES_IN, expiresIn)
            }
            localStorage.setItem(PREV_PATH, '/certificates');
            return <Redirect to={{
                pathname: "/certificates",
                state: { from: this.props.location }
            }}/>;
        } else {
            return <Redirect to={{
                pathname: "/login",
                state: { 
                    from: this.props.location,
                    error: error 
                }
            }}/>; 
        }
    }
}

export default OAuth2RedirectHandler;
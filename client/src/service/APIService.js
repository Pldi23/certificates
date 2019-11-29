import {
    API_BASE_URL,
    ACCESS_TOKEN,
    REFRESH_TOKEN,
    ACCESS_TOKEN_EXPIRES_IN,
    REFRESH_TOKEN_URL,
    COOKIES_XSRF,
    AUTHORIZATION_HEADER,
    REFRESH_HEADER,
    EXPIRES_IN_HEADER,
    COOKIES_LOCALE,
    APP_DEFAULT_LOCALE,
    XSRF_HEADER, USER_SELF_URL, LOGIN_URL, CERTIFICATES_URL, TAGS_ALL_URL, SIGN_UP_URL, POST_ORDERS_URL
} from '../constants';
import Alert from "react-s-alert";
import {getMessage} from "../app/Message";

const request = (options, props) => {
    const headers = new Headers({
        'Content-Type': 'application/json',
    });
    const {cookies} = props;
    if (localStorage.getItem(ACCESS_TOKEN)) {
        if (localStorage.getItem(ACCESS_TOKEN_EXPIRES_IN) && localStorage.getItem(REFRESH_TOKEN) &&
            localStorage.getItem(ACCESS_TOKEN_EXPIRES_IN) < Date.now()) {
            return fetch(API_BASE_URL + REFRESH_TOKEN_URL, {
                method: 'POST',
                credentials: 'include',
                headers: {
                    'RefreshToken': localStorage.getItem(REFRESH_TOKEN),
                    'X-XSRF-TOKEN': cookies.get(COOKIES_XSRF),
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
            })
                .then(response => {
                    if (response.ok) {
                        localStorage.setItem(ACCESS_TOKEN, response.headers.get(AUTHORIZATION_HEADER));
                        localStorage.setItem(REFRESH_TOKEN, response.headers.get(REFRESH_HEADER));
                        localStorage.setItem(ACCESS_TOKEN_EXPIRES_IN, response.headers.get(EXPIRES_IN_HEADER));
                        headers.append(AUTHORIZATION_HEADER, response.headers.get(AUTHORIZATION_HEADER));
                        console.log('token setted');
                        console.log(headers.get(AUTHORIZATION_HEADER));
                        if (options.method !== 'GET') {
                            headers.append(XSRF_HEADER, cookies.get(COOKIES_XSRF));

                        }
                        headers.append("Accept-Language", cookies.get(COOKIES_LOCALE) || APP_DEFAULT_LOCALE);
                        const defaults = {
                            credentials: 'include',
                            headers: headers
                        };
                        options = Object.assign({}, defaults, options);
                        return fetch(options.url, options)


                    } else {

                        console.log('Token failed to refresh ' + response + ' , ' + localStorage.getItem(ACCESS_TOKEN));
                        return Promise.reject("No valid refresh token set.");

                    }
                }).catch(error => {
                console.log(
                    (error && error.message) || 'could not refresh token');
            });
        } else {
            headers.append(AUTHORIZATION_HEADER, localStorage.getItem(ACCESS_TOKEN));
            if (options.method !== 'GET') {
                headers.append(XSRF_HEADER, cookies.get(COOKIES_XSRF));

            }
            headers.append("Accept-Language", cookies.get(COOKIES_LOCALE) || APP_DEFAULT_LOCALE);
            const defaults = {
                credentials: 'include',
                headers: headers
            };
            options = Object.assign({}, defaults, options);
            return fetch(options.url, options)
        }
    } else {
        if (options.method !== 'GET') {
            headers.append(XSRF_HEADER, cookies.get(COOKIES_XSRF));

        }
        headers.append("Accept-Language", cookies.get(COOKIES_LOCALE) || APP_DEFAULT_LOCALE);
        const defaults = {
            credentials: 'include',
            headers: headers
        };
        options = Object.assign({}, defaults, options);
        return fetch(options.url, options)
    }

};

export function getCurrentUser(props) {
    if (!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }
    return request({
        url: API_BASE_URL + USER_SELF_URL,
        method: 'GET',

    }, props).then(response => {
            return response.json().then(json => {
                if (!response.ok) {
                    return Promise.reject(json);
                }
                return json;
            });
        }
    );
}

export function getOrdersSelf(props, href) {
    let url = new URL(API_BASE_URL + href);
    if (url.searchParams.get('size') > 100) {
        url.searchParams.set('size', '100');
    }
    return request({
        url: url,
        method: 'GET'
    }, props).then(response => {
            return response.json().then(json => {
                if (!response.ok) {
                    Alert.closeAll();
                    Alert.error(getMessage(props, 'badOrdersRequest'));
                    return Promise.reject(json);
                }
                return json;
            });
        }
    )
}

export function getCertificatesByHref(props, href) {
    let url = new URL(API_BASE_URL + href);
    if (url.searchParams.get('size') > 100) {
        url.searchParams.set('size', '100');
    }
    return request({
        url: url,
        method: 'GET',
    }, props).then(response => {
            return response.json().then(json => {
                if (!response.ok) {
                    Alert.closeAll();
                    Alert.error(getMessage(props, 'badCertificatesRequest'));
                    return Promise.reject(json);
                }
                return json;
            });
        }
    )
}

export function login(email, password, props) {
    let url = new URL(API_BASE_URL + LOGIN_URL),
        params = {username: email, password: password};
    Object.keys(params).forEach(key => url.searchParams.append(key, params[key]));
    return request({
        url: url,
        method: 'POST',
    }, props)
}

export function loadCertificate(certificateJson, props) {
    return request({
        url: API_BASE_URL + CERTIFICATES_URL,
        method: 'POST',
        body: certificateJson,
    }, props)
}

export function updateCertificate(certificateJson, props, href) {
    return request({
        url: href,
        method: 'PUT',
        body: certificateJson
    }, props)
}

export function deleteCertificate(props, href) {
    return request({
        url: href,
        method: 'DELETE'
    }, props)
}

export function getTags(props) {

    return request({
        url: API_BASE_URL + TAGS_ALL_URL,
        method: 'GET'
    }, props)
        .then(response => {
            if (!response.ok)  {
                return Promise.reject(response.json())
            }
            return response.json()
        });
}

export function signup(userJson, props) {
    return request({
        url: API_BASE_URL + SIGN_UP_URL,
        method: 'POST',
        body: userJson,
    }, props);
}

export function postOrder(orderJson, props) {
    return request({
        url: API_BASE_URL + POST_ORDERS_URL,
        method: 'POST',
        body: orderJson,
    }, props)
}

export function getCertificateById(id, props) {
    return request({
        url: API_BASE_URL + CERTIFICATES_URL + '/' + id,
        method: 'GET',
    }, props);
}
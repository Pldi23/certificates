import {API_BASE_URL, ACCESS_TOKEN, REFRESH_TOKEN, ACCESS_TOKEN_EXPIRES_IN} from '../constants';
import Alert from "react-s-alert";

const request = (options, props) => {
    const headers = new Headers({
        'Content-Type': 'application/json',
    });
    const {cookies} = props;
    if (localStorage.getItem(ACCESS_TOKEN)) {
        if (localStorage.getItem(ACCESS_TOKEN_EXPIRES_IN) && localStorage.getItem(REFRESH_TOKEN) &&
            localStorage.getItem(ACCESS_TOKEN_EXPIRES_IN) < Date.now()) {
            fetch(API_BASE_URL + '/authenticate/refresh-token', {
                method: 'POST',
                credentials: 'include',
                headers: {
                    'RefreshToken': localStorage.getItem(REFRESH_TOKEN),
                    'X-XSRF-TOKEN': cookies.get('XSRF-TOKEN'),
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
            })
                .then(response => {
                    if (response.ok) {
                        localStorage.setItem(ACCESS_TOKEN, response.headers.get("Authorization"));
                        localStorage.setItem(REFRESH_TOKEN, response.headers.get("RefreshToken"));
                        localStorage.setItem(ACCESS_TOKEN_EXPIRES_IN, response.headers.get("ExpiresIn"));
                        headers.append('Authorization', response.headers.get("Authorization"));
                        props.loginHandler(true);
                    } else {
                        console.log('respons not ok, something go wrong ' + response.status + ' , ' + response + ' , ' + localStorage.getItem(ACCESS_TOKEN));
                    }
                })
                .catch(error => {
                    console.log(
                        (error && error.message) || 'could not refresh token');
                });
        } else {
            headers.append('Authorization', localStorage.getItem(ACCESS_TOKEN))
        }
    }

    if (options.method !== 'GET') {
        headers.append('X-XSRF-TOKEN', cookies.get('XSRF-TOKEN'));

    }
    headers.append("Accept-Language", cookies.get('locale') ? cookies.get('locale') : 'en');
    const defaults = {
        credentials: 'include',
        headers: headers
    };
    options = Object.assign({}, defaults, options);
    return fetch(options.url, options)

};

export function getCurrentUser(props) {
    if (!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }
    return request({
        url: API_BASE_URL + "/users/self",
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

export function getCertificates(props) {
    return request({
        url: API_BASE_URL + "/certificates",
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

export function getCertificatesByQuery(props, options) {
    let url = new URL(API_BASE_URL + "/certificates?");
        // params = {page: page ? page : 1, size: size ? size : 5};
    Object.keys(options).forEach(key => url.searchParams.append(key, options[key]));
    // Object.keys(params).forEach(key => url.searchParams.append(key, params[key]));
    return request({
        url: url,
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

export function getCertificatesByHref(props, href) {
    return request({
        url: API_BASE_URL + href,
        method: 'GET',
    }, props).then(response => {
            return response.json().then(json => {
                if (response.status === 400) {
                    let message = JSON.stringify(json.messages);
                    Alert.error(message.substring(1, message.length - 1));
                    return []
                }
                if (!response.ok) {
                    return Promise.reject(json);
                }
                return json;
            });
        }
    )
}

export function login(email, password,  props) {
    let url = new URL(API_BASE_URL + "/authenticate?"),
        params = {username: email, password: password};
    Object.keys(params).forEach(key => url.searchParams.append(key, params[key]));
    return request({
        url: url,
        method: 'POST',
    }, props)
}

export function loadCertificate(certificateJson, props) {
    return request({
        url: API_BASE_URL + "/certificates",
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
        url: API_BASE_URL + "/tags?page=1&size=10000",
        method: 'GET'
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

export function signup(userJson, props) {
    return request({
        url: API_BASE_URL + "/users",
        method: 'POST',
        body: userJson,
    }, props);
}

export function postOrder(orderJson, props) {
    return request({
        url: API_BASE_URL + "/orders",
        method: 'POST',
        body: orderJson,
    }, props)
}

// export function search(parameters) {
//     let url = new URL(API_BASE_URL + "/certificates?name=l:");
//     parameters.forEach(parameter => url.append(parameter, ','));
//     return request({
//         url: url,
//         method: 'GET',
//     });
// }


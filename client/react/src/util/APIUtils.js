import {API_BASE_URL, ACCESS_TOKEN, REFRESH_TOKEN, ACCESS_TOKEN_EXPIRES_IN} from '../constants';

const request = (options, props) => {
    const headers = new Headers({
        'Content-Type': 'application/json',
    });
    if (localStorage.getItem(ACCESS_TOKEN)) {
        if (localStorage.getItem(ACCESS_TOKEN_EXPIRES_IN) && localStorage.getItem(REFRESH_TOKEN) &&
            localStorage.getItem(ACCESS_TOKEN_EXPIRES_IN) < Date.now()) {
            fetch(API_BASE_URL + '/authenticate/refresh-token', {
                method: 'POST',
                credentials: 'include',
                headers: {
                    'RefreshToken': localStorage.getItem(REFRESH_TOKEN),
                    'X-XSRF-TOKEN': options.csrfToken,
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

    if (options.csrfToken && options.method !== 'GET') {
        headers.append('X-XSRF-TOKEN', options.csrfToken);

    }

    const defaults = {
        credentials: 'include',
        headers: headers
    };
    options = Object.assign({}, defaults, options);
    return fetch(options.url, options)

};

export function getCurrentUser(csrfToken, props) {
    if (!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }
    console.log('getting user with token ' + localStorage.getItem(ACCESS_TOKEN));
    return request({
        url: API_BASE_URL + "/users/self",
        method: 'GET',
        csrfToken: csrfToken

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

export function getCertificates(csrfToken, props) {
    console.log('getting certificates wit token ' + localStorage.getItem(ACCESS_TOKEN));
    return request({
        url: API_BASE_URL + "/certificates",
        method: 'GET',
        csrfToken: csrfToken
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

export function login(email, password, csrfToken, props) {
    let url = new URL(API_BASE_URL + "/authenticate?"),
        params = {username: email, password: password};
    Object.keys(params).forEach(key => url.searchParams.append(key, params[key]));
    return request({
        url: url,
        method: 'POST',
        csrfToken: csrfToken
    }, props)

}

export function signup(userJson, csrfToken) {
    return request({
        url: API_BASE_URL + "/users",
        method: 'POST',
        body: userJson,
        csrfToken: csrfToken
    });
}

// export function isAccessTokenExpired() {
//     if (localStorage.getItem(ACCESS_TOKEN) && localStorage.getItem(ACCESS_TOKEN) != null) {
//         if (localStorage.getItem(ACCESS_TOKEN_EXPIRES_IN) && localStorage.getItem(REFRESH_TOKEN) &&
//             localStorage.getItem(ACCESS_TOKEN_EXPIRES_IN) < Date.now()) {
//             console.log("need to refresh");
//             return true;
//         }
//     }
//     return false;
// }

// export function refreshToken(csrfToken) {
//     // if (localStorage.getItem(ACCESS_TOKEN) && localStorage.getItem(ACCESS_TOKEN) != null) {
//     //     if (localStorage.getItem(ACCESS_TOKEN_EXPIRES_IN) && localStorage.getItem(REFRESH_TOKEN) &&
//     //         localStorage.getItem(ACCESS_TOKEN_EXPIRES_IN) < Date.now()) {
//     console.log('Api utils refreshing token start, expired bearer: ' + localStorage.getItem(ACCESS_TOKEN));
//     fetch(API_BASE_URL + '/authenticate/refresh-token', {
//         method: 'POST',
//         credentials: 'include',
//         headers: {
//             'RefreshToken': localStorage.getItem(REFRESH_TOKEN),
//             'X-XSRF-TOKEN': csrfToken,
//             'Accept': 'application/json',
//             'Content-Type': 'application/json',
//         },
//     }).then(response => {
//         if (response.ok) {
//             localStorage.setItem(ACCESS_TOKEN, response.headers.get("Authorization"));
//             localStorage.setItem(REFRESH_TOKEN, response.headers.get("RefreshToken"));
//             localStorage.setItem(ACCESS_TOKEN_EXPIRES_IN, response.headers.get("ExpiresIn"));
//             console.log('respons ok new access token setted ' + localStorage.getItem(ACCESS_TOKEN));
//
//         } else {
//             console.log('respons not ok, something go wrong ' + response.status + ' , ' + response + ' , ' + localStorage.getItem(ACCESS_TOKEN));
//
//             // response.json().then(response => {
//             //     console.log(JSON.stringify(response.messages));
//             // });
//         }
//     })
//         .catch(error => {
//             console.log(
//                 (error && error.message) || 'could not refresh token');
//         });
//     //     }
//     //     console.log('Api utils no refreshing token needed (token not expired)' + localStorage.getItem(ACCESS_TOKEN));
//     // }
//     // console.log('Api utils no refreshing token needed (no token?)' + localStorage.getItem(ACCESS_TOKEN));
// }


export function search(parameters) {
    let url = new URL(API_BASE_URL + "/certificates?name=l:");
    parameters.forEach(parameter => url.append(parameter, ','));
    return request({
        url: url,
        method: 'GET',
    });
}


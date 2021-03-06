export const API_BASE_URL = 'http://localhost:8080';
export const CERTIFICATES_DEFAULT_REQUEST_URL = '/certificates?sort=creationdate';
export const REFRESH_TOKEN_URL = '/authenticate/refresh-token';
export const USER_SELF_URL = "/users/self";
export const LOGIN_URL = "/authenticate?";
export const CERTIFICATES_URL = "/certificates";
export const TAGS_ALL_URL = "/tags?page=1&size=100000";
export const SIGN_UP_URL = "/users";
export const POST_ORDERS_URL = "/orders";
export const ORDERS_SELF_URL = '/orders/self';
export const SIZE_API_PARAMETER = 'size=';
export const PAGE_API_PARAMETER = 'page=';
export const CERTIFICATE_NAME_API_PARAMETER = 'name=';


export const ACCESS_TOKEN = 'accessToken';
export const REFRESH_TOKEN = 'refreshToken';
export const ACCESS_TOKEN_EXPIRES_IN = 'expiresIn';
export const COOKIES_XSRF = 'XSRF-TOKEN';
export const COOKIES_LOCALE = 'locale';
export const COOKIES_CART = 'cart';
export const COOKIES_VIEWED_CERTIFICATES = 'seen';
export const APP_DEFAULT_LOCALE = 'en';
export const AUTHORIZATION_HEADER = 'Authorization';
export const REFRESH_HEADER = "RefreshToken";
export const EXPIRES_IN_HEADER = "ExpiresIn";
export const XSRF_HEADER = 'X-XSRF-TOKEN';

export const PREV_PATH = 'prev_path';
export const CERTIFICATES_HREF = 'cert_href';
export const SEARCH_PARAMETERS = 'params';

export const EMAIL_REGEX_PATTERN = "[A-Za-z0-9]{1,64}@[A-Za-z]{1,254}\u002E[A-Za-z]{1,5}";
export const PASSWORD_REGEX_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+={};:><.,/?`~±§-])(?=[^\r\n\t\f\v]+$).{8,20}$";
export const CERTIFICATE_NAME_REGEX_PATTERN = "^([\\w\\s-]{1,30})$";
export const CERTIFICATE_PRICE_PATTERN = "[0-9]+(.[0-9][0-9]?)?";
export const SEARCH_REGEX_PATTERN = "([\\w\\s]+)|(\\#\u0028\\w+(\\s?)\\w+\u0029)|(\\$\u0028([<>]?)\\d+\u002E?\\d?\\d?(.\\d+\u002E?\\d?\\d?)?\u0029)";

export const OAUTH2_REDIRECT_URI = 'http://localhost:3000/oauth2/redirect';

export const GOOGLE_AUTH_URL = API_BASE_URL + '/oauth2/authorize/google?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const FACEBOOK_AUTH_URL = API_BASE_URL + '/oauth2/authorize/facebook?redirect_uri=' + OAUTH2_REDIRECT_URI;




export const ROLE_ADMIN = 'ROLE_ADMIN';
export const ROLE_USER = 'ROLE_USER';



export const API_BASE_URL = 'http://localhost:8080';
export const CERTIFICATES_DEFAULT_REQUEST_URL = '/certificates?sort=creationdate';
export const ACCESS_TOKEN = 'accessToken';
export const REFRESH_TOKEN = 'refreshToken';
export const ACCESS_TOKEN_EXPIRES_IN = 'expiresIn';

export const PREV_PATH = 'prev_path';
export const CERTIFICATES_HREF = 'cert_href';

export const EMAIL_REGEX_PATTERN = "[A-Za-z0-9]{1,64}@[A-Za-z]{1,254}\u002E[A-Za-z]{1,5}";
export const PASSWORD_REGEX_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+={};:><.,/?`~±§-])(?=[^\r\n\t\f\v]+$).{8,20}$";
export const CERTIFICATE_NAME_REGEX_PATTERN = "([\\w-]+(?: [\\w-]+)+)|([\\w-]+)";
export const CERTIFICATE_PRICE_PATTERN = "[0-9]+(.[0-9][0-9]?)?";
export const SEARCH_REGEX_PATTERN = "([\\w ]+)|(\\#\u0028\\w+( ?)\\w+\u0029)|(\\$\u0028([<>]?)\\d+\u002E?\\d?\\d?(.\\d+\u002E?\\d?\\d?)?\u0029)";
// export const SEARCH_PRICE_REGEX_PATTERN = "(\$\u0028([<>]?)\\d+\u002E?\\d?\\d?(.\\d+\u002E?\\d?\\d?)?\u0029)";
// export const SEARCH_TAG_REGEX_PATTERN = "(?<!\u0028)\\b[\\w\\s]+\\b(?![\\w\\s]*[\u0029])";
// export const SEARCH_NAME_OR_DESCRIPTION_REGEX_PATTERN = "(?<!\u0028)\\b[\\w\\s]+\\b(?![\\w\\s]*[\u0029])";

export const OAUTH2_REDIRECT_URI = 'http://localhost:3000/oauth2/redirect';

export const GOOGLE_AUTH_URL = API_BASE_URL + '/oauth2/authorize/google?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const FACEBOOK_AUTH_URL = API_BASE_URL + '/oauth2/authorize/facebook?redirect_uri=' + OAUTH2_REDIRECT_URI;

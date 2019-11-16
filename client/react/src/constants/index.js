export const API_BASE_URL = 'http://localhost:8080';
export const CERTIFICATES_DEFAULT_REQUEST_URL = '/certificates?sort=creationdate';
export const ACCESS_TOKEN = 'accessToken';
export const REFRESH_TOKEN = 'refreshToken';
export const ACCESS_TOKEN_EXPIRES_IN = 'expiresIn';

export const PREV_PATH = 'prev_path';
export const CERTIFICATES_HREF = 'cert_href';

export const OAUTH2_REDIRECT_URI = 'http://localhost:3000/oauth2/redirect';

export const GOOGLE_AUTH_URL = API_BASE_URL + '/oauth2/authorize/google?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const FACEBOOK_AUTH_URL = API_BASE_URL + '/oauth2/authorize/facebook?redirect_uri=' + OAUTH2_REDIRECT_URI;

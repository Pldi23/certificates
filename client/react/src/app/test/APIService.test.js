import {login} from '../../service/APIService'
import React from "react";
import {API_BASE_URL} from "../../constants";
import {Cookies} from "react-cookie";
const fetchMock = require('fetch-mock');

describe('Api fetch service', () => {
    let mockProps;
    beforeEach(() => {
        mockProps = jest.fn();
    });
    it('should fetch data', done => {
        const email = 'pldi@mail.ru';
        const password = 'Qwertyui1!';
        fetchMock.mock(API_BASE_URL + '/authenticate?' + email + '&' + password, 200);
        const mockFn = jest.fn();
        const cookies = new Cookies('XSRF-TOKEN', '0ea945ee-a453-4e58-8cf6-87aa1cbc19e8');
        // cookies.get('XSRF-TOKEN') = mockFn;

        const response = login(email, password, cookies);
        assert(response.ok);
        fetchMock.restore();

    });
});
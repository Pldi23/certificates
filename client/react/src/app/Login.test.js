import React from 'react'
import LoginForm from "../user/login/LoginForm";
import {describe} from "mocha";
const mocha = require('mocha');
const chai = require('chai');
let assert = require('assert');


describe('LoginPage', function () {
    let loginPage = null;
    beforeEach(function () {
        loginPage = new LoginForm;
    });


    it('should render successfully', function () {
        const event = {
            target: {
                value: 'pldi@mail.ru'
            }
        };
        loginPage.handleInputEmail(event);
        const actual = loginPage.state.email;
        const expected = {
            value: 'pldi@mail.ru',
            isValid: true
        };
        assert.equal(actual, expected);
    });
});
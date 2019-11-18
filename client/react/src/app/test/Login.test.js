import React from "react";
import { shallow } from "enzyme";
import LoginForm from "../../user/login/LoginForm";
import { configure } from "enzyme";
import Adapter from "enzyme-adapter-react-16";

configure({ adapter: new Adapter() });

describe("handleChange", () => {

    let wrapper;
    let mockRouteHandler;
    beforeEach(() => {
        mockRouteHandler = jest.fn();
        wrapper = shallow(<LoginForm routeHandler={mockRouteHandler}/>);
    });

    it("should change state on email", () => {
        const mockEvent = {
            target: {
                value: "pldi@mail.ru"
            }
        };
        const expected = {
            email: {
                value: 'pldi@mail.ru',
                isValid: true
            },
            password: {
                value: '',
                isValid: false
            }
        };
        wrapper.instance().handleInputEmail(mockEvent);

        expect(wrapper.state()).toEqual(expected);
    });

    it("should change state on email but isValid should be false", () => {
        const mockEvent = {
            target: {
                value: "@"
            }
        };
        const expected = {
            email: {
                value: '@',
                isValid: false
            },
            password: {
                value: '',
                isValid: false
            }
        };
        wrapper.instance().handleInputEmail(mockEvent);

        expect(wrapper.state()).toEqual(expected);
    });

    it("should change state on password", () => {
        const mockEvent = {
            target: {
                value: "Qwertyui1!"
            }
        };
        const expected = {
            email: {
                value: '',
                isValid: false
            },
            password: {
                value: 'Qwertyui1!',
                isValid: true
            }
        };
        wrapper.instance().handleInputPassword(mockEvent);

        expect(wrapper.state()).toEqual(expected);
    });

    it("should change state on password but isValid should be false", () => {
        const mockEvent = {
            target: {
                value: "Q"
            }
        };
        const expected = {
            email: {
                value: '',
                isValid: false
            },
            password: {
                value: 'Q',
                isValid: false
            }
        };
        wrapper.instance().handleInputPassword(mockEvent);

        expect(wrapper.state()).toEqual(expected);
    });
});
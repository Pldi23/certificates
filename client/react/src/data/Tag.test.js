import React from 'react';
import ReactDOM from 'react-dom';
import { configure, shallow } from 'enzyme';
import { expect } from 'chai'
import Tag from './Tag';
import Adapter from 'enzyme-adapter-react-16'


configure({ adapter: new Adapter() });


describe('Tag component testing', function() {
    it('renders Tag', function() {
        const wrapper = shallow(<Tag />);
        // expect(wrapper.props().tagSearchHandler).to.be.defined;
        // expect(wrapper.props().tag).to.be.defined;
        // const expected = <h1 className='App-title'>Welcome to React</h1>;
        // const expected = <button className={'badge badge-info'} onClick={() => this.props.tagSearchHandler(this.props.tag.tag.title)}>
        //     {this.props.tag.tag.title}
        // </button>
        // expect(wrapper).to.contain('button');
        expect(wrapper.find('button')).to.have.length(1);
    });

    // chai.use(chaiEnzyme())
});
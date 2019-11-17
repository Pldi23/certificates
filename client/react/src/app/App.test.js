import React from 'react';
import ReactDOM from 'react-dom';
import { configure, shallow } from 'enzyme';
import { expect } from 'chai';
import App from './App';
import Adapter from 'enzyme-adapter-react-16'
configure({ adapter: new Adapter() });

it('renders without crashing', () => {
  const div = document.createElement('div');
  ReactDOM.render(<App />, div);
  ReactDOM.unmountComponentAtNode(div);
});


// describe('App component testing', function() {
//   it('renders welcome message', function() {
//     const wrapper = shallow(<App />);
//     // const expected = <h1 className='App-title'>Welcome to React</h1>;
//     const expected = <div className="app"></div>
//     expect(wrapper.contains(expected)).to.equal(true);
//   });
// });



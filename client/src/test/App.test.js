import React from 'react';
import ReactDOM from 'react-dom';
import { configure} from 'enzyme';
import App from '../app/App';
import Adapter from 'enzyme-adapter-react-16'
configure({ adapter: new Adapter() });

it('renders without crashing', () => {
  const div = document.createElement('div');
  ReactDOM.render(<App />, div);
  ReactDOM.unmountComponentAtNode(div);
});




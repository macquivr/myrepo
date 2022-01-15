import React from 'react';

import Updateu from '../../components/Updateu'
import Dropdown from 'react-dropdown';
import 'react-dropdown/style.css';
import Whichdate from '../values/whichdate';

function Hwhichdate(props) {
  const handleChange = (event) => {
    Updateu({ session: props.session, ctype: 'USE_DATE', data: event.value})
  };

  return (
    <Dropdown options={Whichdate} onChange={handleChange} value={Whichdate[0]} placeholder="Select an option" />
  );
}

export default Hwhichdate;
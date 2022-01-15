import React from 'react';

import Updateu from '../../components/Updateu'
import Dropdown from 'react-dropdown';
import 'react-dropdown/style.css';
import Consolidates from '../values/consolidate';

function Hconsolidate(props) {
  const handleChange = (event) => {
    Updateu({ session: props.session, ctype: 'CONSOLIDATE', data: event.value})
  };

  return (
    <Dropdown options={Consolidates} onChange={handleChange} value={Consolidates[0]} placeholder="Select an option" />
  );
}

export default Hconsolidate;
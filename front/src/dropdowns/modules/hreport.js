import React from 'react';

import Updateu from '../../components/Updateu'
import Dropdown from 'react-dropdown';
import 'react-dropdown/style.css';
import Rvalues from '../values/rvalues';

function Hreport(props) {
  const handleChange = (event) => {
    Updateu({ session: props.session, ctype: 'REPORT', data: event.value})
  };

  return (
    <Dropdown options={Rvalues} onChange={handleChange} value={Rvalues[0]} placeholder="Select an option" />
  );
}

export default Hreport;
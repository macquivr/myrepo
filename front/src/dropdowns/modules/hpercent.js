import React from 'react';

import Updateu from '../../components/Updateu'
import Dropdown from 'react-dropdown';
import 'react-dropdown/style.css';
import Percentvalues from '../values/percentvalues';

function Hpercent(props) {
  const handleChange = (event) => {
    Updateu({ session: props.session, ctype: 'PERCENT', data: event.value})
  };

  return (
    <Dropdown options={Percentvalues} onChange={handleChange} value={Percentvalues[0]} placeholder="Select an option" />
  );
}

export default Hpercent;
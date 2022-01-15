import React from 'react';

import Updateu from '../../components/Updateu'
import Dropdown from 'react-dropdown';
import 'react-dropdown/style.css';
import Stypes from '../values/stypes';

function Hstype(props) {
  const handleChange = (event) => {
    Updateu({ session: props.session, ctype: 'STYPE', data: event.value})
  };

  return (
    <Dropdown options={Stypes} onChange={handleChange} value={Stypes[0]} placeholder="Select an option" />
  );
}

export default Hstype;
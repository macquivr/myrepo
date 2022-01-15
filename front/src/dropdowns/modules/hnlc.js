import React from 'react';

import Updateu from '../../components/Updateu'
import Dropdown from 'react-dropdown';
import 'react-dropdown/style.css';
import Nlcvalues from '../values/nlcvalues';

function Hnlc(props) {
  const handleChange = (event) => {
    Updateu({ session: props.session, ctype: 'NLC', data: event.value})
  };

  return (
    <Dropdown options={Nlcvalues} onChange={handleChange} value={Nlcvalues[0]} placeholder="Select an option" />
  );
}

export default Hnlc;
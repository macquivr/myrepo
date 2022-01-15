import React from 'react';

import Updateu from '../../components/Updateu'
import Dropdown from 'react-dropdown';
import 'react-dropdown/style.css';
import Accounts from '../values/accounts';

function Haccounts(props) {
  const handleChange = (event) => {
    Updateu({ session: props.session, ctype: 'LTYPE', data: event.value})
  };

  return (
    <Dropdown options={Accounts} onChange={handleChange} value={Accounts[0]} placeholder="Select an option" />
  );
}

export default Haccounts;
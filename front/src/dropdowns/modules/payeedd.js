import React, { useEffect, useState } from 'react';

import UpdateImport from '../../components/UpdateImport'
import Dropdown from 'react-dropdown';
import 'react-dropdown/style.css';

function Payeedd(props) {
   const [appState, setAppState] = useState({
      loading: false,
      mydata: null,
    });

    useEffect(() => {
      setAppState({ loading: true });
      const datasrc = '/SpringBootRepository/payees';

      fetch(datasrc)
        .then((res) => res.json())
        .then((data) =>
           setAppState({ loading: false, mydata: data })
        )

    }, [setAppState]);

  const handleChange = (event) => {
    UpdateImport({ session: props.session, ctype: "CHECK", data: event.value})
  };

  if (appState.mydata != null) {
    return (
      <Dropdown options={appState.mydata} onChange={handleChange} value={appState.mydata[0]} placeholder="Select an option" />
    );
  }

  return (
    <h1>Not Ready</h1>
  );
}

export default Payeedd;
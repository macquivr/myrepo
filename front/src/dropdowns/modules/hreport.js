import React, { useEffect, useState }  from 'react';

import Updateu from '../../components/Updateu'
import Dropdown from 'react-dropdown';
import 'react-dropdown/style.css';
import Rvalues from '../values/rvalues';

function Hreport(props) {

   const [appState, setAppState] = useState({
        loading: false,
        mydata: null,
      });

      useEffect(() => {
        setAppState({ loading: true });
        const datasrc = '/SpringBootRepository/reports';

        fetch(datasrc)
          .then((res) => res.json())
          .then((data) =>
             setAppState({ loading: false, mydata: data })
          )

      }, [setAppState]);


  const handleChange = (event) => {
    Updateu({ session: props.session, ctype: 'REPORT', data: event.value})
  };

 if (appState.mydata != null) {
    return (
      <Dropdown options={appState.mydata.rvalue} onChange={handleChange} value={appState.mydata.rvalue[0]} placeholder="dropdown" />
    );
  }

  return (
    <h1>Not Ready</h1>
  );

  /*
  return (
    <Dropdown options={Rvalues} onChange={handleChange} value={Rvalues[0]} placeholder="Select an option" />
  );
  */
}

export default Hreport;
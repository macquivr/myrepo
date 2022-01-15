import React, { useEffect, useState } from 'react';
import { Typeahead } from 'react-bootstrap-typeahead';
import Percentvalues from '../dropdowns/values/percentvalues';
import Updateu from './Updateu'
import Dropdown from 'react-dropdown';

function NLCData(props) {
  const [singleSelections, setSingleSelections] = useState([]);
  const [appState, setAppState] = useState({
    loading: false,
    reset: null,
    all: null,
    state: null
  });

  useEffect(() => {
     setAppState({ loading: true });
        const datasrc = '/SpringBootRepository/nlc/' + props.session;

        fetch(datasrc)
          .then((res) => res.json())
          .then((data) => {
            setAppState({ loading: false, all: data, reset: null, state: data.state })
          })
  }, [setAppState]);

  const handleTmpChange = (event) => {
    const datasrc = '/SpringBootRepository/nlc/' + props.session;

    fetch(datasrc)
      .then((res) => res.json())
      .then((data) => {
        setAppState({ loading: false, all: data, reset: null, state: data.state })
    })
  }

  const handleChange = (s) => {
    Updateu({ session: props.session, ctype: 'NLCV', data: s[0].name})
  }

  if (appState.all != null) {
   if (appState.state === "NONE")  {
     return (
       <Dropdown options={Percentvalues} onChange={handleTmpChange} value={Percentvalues[0]} placeholder="Select an option" />
     );
   }
   if (appState.state === "NAME")  {
      return (
            <Typeahead
                 id="typeahead"
                 labelKey="name"
                 onChange={handleChange}
                 options={appState.all.names}
                 placeholder="placeholder"
                 selected={singleSelections}
            />);
    }

    if (appState.state === "LOCATION") {
      return (
        <Typeahead
          id="typeahead"
          labelKey="name"
          onChange={handleChange}
          options={appState.all.locations}
          placeholder="placeholder"
          selected={singleSelections}
        />);
    }

    if (appState.state === "CATEGORY") {
       return (
         <Typeahead
            id="typeahead"
            labelKey="name"
            onChange={handleChange}
            options={appState.all.categories}
            placeholder="placeholder"
            selected={singleSelections}
         />);
    }
  }

  return (
    <h1>Not Ready</h1>
  );
}

export default NLCData;
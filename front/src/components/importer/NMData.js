import React, { useEffect, useState } from 'react';
import { Typeahead } from 'react-bootstrap-typeahead';

function NMData(props) {
  const [singleSelections, setSingleSelections] = useState([]);
  const [appState, setAppState] = useState({
    loading: false,
    reset: null,
    all: null,
    state: null
  });

  useEffect(() => {
     setAppState({ loading: true });
        const datasrc = '/SpringBootRepository/import/' + props.session + '/nameLocCat';

        fetch(datasrc)
          .then((res) => res.json())
          .then((data) => {
            setAppState({ loading: false, all: data, reset: null, state: data.state })
          })
  }, [setAppState]);

  const handleChange = (s) => {

     const requestOptions = {
       method: 'POST',
       headers: { 'Content-Type': 'application/json' },
       body: JSON.stringify({ session: props.session, type: "NEWL", data: s[0].name })
     };
     const apiUrl = '/SpringBootRepository/import/' + props.session + '/update';

     fetch(apiUrl, requestOptions)
       .then((res) => res.json())
       .then((response) => {
         if (response.status) {
           alert("OK " + response.message);
           let svar = null;
           if (response.message === "DONE") {
             svar = props.state;
           }

           setAppState({ loading: false, all: appState.all, reset: svar, state: response.message });
         } else
            alert("ERROR " + response.message)
       }
    )
  }

  if (appState.all != null) {
    if (props.state != null) {
      if ((props.state === "RESET1") || (props.state === "RESET2")) {
        if (appState.state === "DONE") {
          if ((appState.reset === null) || (appState.reset != props.state)) {
            setAppState({ loading: false, all: appState.all, reset: props.state, state: "NAME" });
          }
        }
      }
   }

   if ((appState.state === "NAME") && ((props.state == null) || (props.state === "RESET1") || (props.state === "RESET2"))) {
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

    if (((appState.state === "LOCATION") && ((props.state == null) || (props.state === "RESET1") || (props.state === "RESET2") || (props.state === "LOCATION"))) ||
        ((props.state === "LOCATION") && (appState.state !== "CATEGORY") && (appState.state !== "DONE")))
    {
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

    if ((appState.state === "CATEGORY") || (props.state === "CATEGORY")) {
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
    if ((appState.state === "DONE") || (props.state === "DONE")) {
      return (
          <h1>Done</h1>
       );
    }
  }

  return (
    <h1>Not Ready</h1>
  );
}

export default NMData;
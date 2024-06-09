import React, { useEffect, useState } from 'react';
import { Typeahead } from 'react-bootstrap-typeahead';
import UpdateImport from '../UpdateImport'

function NData(props) {
  const [singleSelections, setSingleSelections] = useState([]);
  const [appState, setAppState] = useState({
    loading: false,
    mydata: null,
  });

  useEffect(() => {
    setAppState({ loading: true });
    const datasrc = '/SpringBootRepository/importLabelsw';

    fetch(datasrc)
      .then((res) => res.json())
      .then((data) =>
         setAppState({ loading: false, mydata: data.data })
      )

  }, [setAppState]);

  const handleChange = (s) => {
     UpdateImport({ session: props.session, ctype: "DUP", data: s[0].name})
  }

  if (appState.mydata != null) {
     if (props.newlabels != null) {
        return (
               <Typeahead
                    id="typeahead"
                    labelKey="name"
                    onChange={handleChange}
                    options={props.newlabels}
                    placeholder="placeholder"
                    selected={singleSelections}
               />);
      }

      return (
       <Typeahead
            id="typeahead"
            labelKey="name"
            onChange={handleChange}
            options={appState.mydata}
            placeholder="placeholder"
            selected={singleSelections}
       />
    );
  }

  return (
    <h1>Not Ready</h1>
  );
}

export default NData;
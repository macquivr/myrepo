import React, { useEffect, useState } from 'react';
import InitI from './importer/InitI';
import FirstI from './importer/FirstI';
import MissingChecks from './importer/MissingChecks';
import MissingLabels from './importer/MissingLabels';
import MissingStypes from './importer/MissingStypes';
import SaveResponse from './importer/SaveResponse';
import Alert from 'react-bootstrap/Alert';

function RouteWB(props) {
 const [appState, setAppState] = useState({
    loading: false,
    mydata: null,
  });

  useEffect(() => {
    setAppState({ loading: true });
    const datasrc = '/SpringBootRepository/importw/' + props.match.params.sessionId;

    fetch(datasrc)
      .then((res) => res.json())
      .then((data) =>
         setAppState({ loading: false, mydata: data })
      )

  }, [setAppState]);

  if (appState.mydata != null) {
      if (appState.mydata.importState === "ERROR") {
        return (
          <Alert variant='warning'>
            {appState.mydata.errMessage}
          </Alert>
        )
      }

      if (appState.mydata.importState === "INIT") {
          return (
             <InitI session={appState.mydata.session}/>
          )
      }

      if (appState.mydata.importState === "MISSING_CHECKS") {
          return (
             <MissingChecks session={appState.mydata.session} mcheck={appState.mydata.mdata}/>
          )
      }

      if (appState.mydata.importState === "MISSING_LABELS") {
          return (
             <MissingLabels session={appState.mydata.session} mlabel={appState.mydata.mdata}/>
          )
      }

      if (appState.mydata.importState === "MISSING_STYPES") {
          return (
             <MissingStypes session={appState.mydata.session} mstype={appState.mydata.mdata}/>
          )
      }

      if ((appState.mydata.importState === "SAVING") || (appState.mydata.importState === "SAVED")) {
          return (
             <SaveResponse state={appState.mydata.importState}/>
          )
      }

      return (
          <FirstI session={appState.mydata.session} state={appState.mydata.importState} />
      )

  }

  return (
    <h1>Not Ready</h1>
  );


}

export default RouteWB
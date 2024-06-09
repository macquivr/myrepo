import React, {  useState } from 'react';
import './App.css';

import 'bootstrap/dist/css/bootstrap.min.css';
import Button from '@material-ui/core/Button';
import ButtonGroup from '@material-ui/core/ButtonGroup';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Tv from './components/Tv';
import Datep from './components/Datep';
import Haccounts from './dropdowns/modules/haccounts'
import Hwhichdate from './dropdowns/modules/hwhichdate'
import Hstype from './dropdowns/modules/hstype'
import Hconsolidate from './dropdowns/modules/hconsolidate'
import Hpercent from './dropdowns/modules/hpercent'
import Hnlc from './dropdowns/modules/hnlc'
import Hreport from './dropdowns/modules/hreport'
import NLCData from './components/NLCData'

function AppI() {
  const [appState, setAppState] = useState({
    loading: false,
    session: null,
    dron: 'Start'
  });

  const importButton = () =>
  {
     window.location.href="http://localhost:3000/home/" + appState.session;
  }

  const importWButton = () =>
  {
     window.location.href="http://localhost:3000/importw/" + appState.session;
  }

  const msession = () => {
    fetch('/SpringBootRepository/msession')
      .then((res) => res.json())
      .then((session) => {
         console.log("NS: " + session.session)
         setAppState({ loading: false, session: session.session, dron: 'On' })
    })
  }

  const report = () => {
      fetch('/SpringBootRepository/report/' + appState.session)
        .then((res) => res.json())
        .then((response) => {
           if (response.status) {
             alert("Response " + response.message);
           } else {
             alert("ERROR " + response.message)
           }
      })
    }

  return (
    <div className='App-top'>
      <AppBar>
        <Toolbar>
          <ButtonGroup>
            <Button onClick={importButton}>Import</Button>
            <Button onClick={importWButton}>ImportW</Button>
            <Button onClick={msession}>NS</Button>
            <Button onClick={() => { alert(appState.session) }}>Session</Button>
          </ButtonGroup>
          <Datep label="start" session={appState.session} ctype="START_DATE"/>
          <Datep label="stop" session={appState.session} ctype="END_DATE"/>
          <Hwhichdate session={appState.session} />
          <Haccounts session={appState.session} />
          <Hstype session={appState.session} />
          <Hconsolidate session={appState.session} />
          <Hpercent session={appState.session} />
          <Hnlc session={appState.session} />
          <NLCData session={appState.session} />
          <Hreport session={appState.session} />
          <Button onClick={report}>Report</Button>
        </Toolbar>
      </AppBar>
      <Toolbar/>
      <Tv session={appState.session}/>
    </div>
  );
}
export default AppI;


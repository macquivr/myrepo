import React, { useState } from 'react';
import MissingLabel from './MissingLabel';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Button from '@material-ui/core/Button';
import NData from './NData'
import NMData from './NMData'

function MissingLabels(props) {
  const [label, setLabel] = useState ({
    mydata: null
  });
  const [inputValue, setInputValue] = useState ('someValue');
  const [labelState, setLabelState] = useState(null);
  const [labels, setLabels] = useState(null);

  const reget = () => {
    const datasrc = '/SpringBootRepository/importLabels';

    fetch(datasrc)
      .then((res) => res.json())
      .then((data) =>
         setLabels(data.data)
      )
  }

  const dupL = () => {
    const datasrc = '/SpringBootRepository/import/' + props.session + '/nextlabel';
      fetch(datasrc)
        .then((res) => res.json())
        .then((data) => {
           setLabel({ mydata: data.mdata })
           if ((labelState == null) || (labelState === "RESET2")) {
             setLabelState("RESET1");
           } else {
             setLabelState("RESET2");
           }
           reget();
        })
  }

  const handleChange = (event) => { setInputValue(event.target.value); }
  const handleSubmit = (event) => {;
    if (labelState === "DONE") {
      alert("Already done.");
      return;
    }
    event.preventDefault();
    const requestOptions = {
           method: 'POST',
           headers: { 'Content-Type': 'application/json' },
           body: JSON.stringify({ session: props.session, type: "NEWL", data: inputValue })
         };
    const apiUrl = '/SpringBootRepository/import/' + props.session + '/update';

    fetch(apiUrl, requestOptions)
     .then((res) => res.json())
     .then((response) => {
       if (response.status) {
         setLabelState(response.message)
         alert("Response " + response.message);
       } else
         alert("ERROR " + response.message)
       }
     )
  }

  if (label.mydata == null) {
    return (
      <div className='App-top'>
        <AppBar>
          <Toolbar>
            <Button onClick={dupL}>Next Label</Button>
            <NData session={props.session} newlabels={labels}/>
            <NMData session={props.session} state={labelState}/>
            <form onSubmit={handleSubmit}>
              <input type="text" value={inputValue} onChange={handleChange} />
            </form>
          </Toolbar>
        </AppBar>
        <Toolbar/>
        <MissingLabel mlabel={props.mlabel}/>
      </div>
    )
  }

  return (
    <div className='App-top'>
      <AppBar>
        <Toolbar>
          <Button onClick={dupL}>Next Label</Button>
          <NData session={props.session} newlabels={labels}/>
          <NMData session={props.session} state={labelState}/>
          <form onSubmit={handleSubmit}>
            <input type="text" value={inputValue} onChange={handleChange} />
          </form>
        </Toolbar>
      </AppBar>
      <Toolbar/>
      <MissingLabel mlabel={label.mydata}/>
    </div>
  )
}
export default MissingLabels
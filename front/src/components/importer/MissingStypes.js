import React, { useState } from 'react';

import MissingStype from './MissingStype';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Stypedd from '../../dropdowns/modules/stypedd'
import Button from '@material-ui/core/Button';

function MissingStypes(props) {
const [stypeState, setStypeState] = useState({
    mydata: null,
  });

 const nextS = () => {
    const datasrc = '/SpringBootRepository/import/' + props.session + '/nextdata';

    fetch(datasrc)
     .then((res) => res.json())
     .then((data) => {
        setStypeState({ mydata: data.mdata })
        if (data.mdata === 'Done.') {
          window.location.href="http://localhost:3000/home/" + props.session;
        }
      }
    )
  }

  if (stypeState.mydata == null) {
    return (
      <div className='App-top'>
        <AppBar>
          <Toolbar>
            <Stypedd session={props.session} mstype={props.mstype} />
            <Button onClick={nextS}>Ok</Button>
          </Toolbar>
        </AppBar>
        <Toolbar/>
        <MissingStype mstype={props.mstype}/>
      </div>
    )
  }

  return (
    <div className='App-top'>
      <AppBar>
        <Toolbar>
          <Stypedd session={props.session} mstype={stypeState.mydata} />
          <Button onClick={nextS}>Ok</Button>
        </Toolbar>
      </AppBar>
      <Toolbar/>
      <MissingStype mstype={stypeState.mydata}/>
    </div>
  )

}
export default MissingStypes;
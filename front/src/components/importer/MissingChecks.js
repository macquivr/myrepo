import React, { useState } from 'react';
import MissingCheck from './MissingCheck';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Payeedd from '../../dropdowns/modules/payeedd'
import Button from '@material-ui/core/Button';

function MissingChecks(props) {
  const [checkState, setCheckState] = useState({
    mydata: null,
  });

 const nextC = () => {
    const datasrc = '/SpringBootRepository/import/' + props.session + '/nextdata';

    fetch(datasrc)
     .then((res) => res.json())
     .then((data) => {
        setCheckState({ mydata: data.mdata })
        if (data.mdata === 'Done.') {
          window.location.href="http://localhost:3000/home/" + props.session;
        }
      }
    )
  }

  if (checkState.mydata == null) {
    return (
      <div className='App-top'>
        <AppBar>
          <Toolbar>
            <Payeedd session={props.session} mcheck={props.mcheck} />
            <Button onClick={nextC}>Ok</Button>
          </Toolbar>
        </AppBar>
        <Toolbar/>
        <MissingCheck mcheck={props.mcheck}/>
      </div>
    )
  }

  return (
    <div className='App-top'>
      <AppBar>
        <Toolbar>
          <Payeedd session={props.session} mcheck={checkState.mydata} />
          <Button onClick={nextC}>Ok</Button>
        </Toolbar>
      </AppBar>
      <Toolbar/>
      <MissingCheck mcheck={checkState.mydata}/>
    </div>
  )
}
export default MissingChecks
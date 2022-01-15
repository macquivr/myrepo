import React, { useEffect, useState } from 'react';

import ReactDOM from 'react-dom';
import FusionCharts from 'fusioncharts';
import Charts from 'fusioncharts/fusioncharts.charts';
import FusionTheme from 'fusioncharts/themes/fusioncharts.theme.fusion';
import ReactFC from 'react-fusioncharts';

function Graphbase(props) {

  const [appState, setAppState] = useState({
    loading: false,
    mydata: null,
  });

  useEffect(() => {
    setAppState({ loading: true });

    const datasrc = '/SpringBootRepository/chart/' + props.urlt + '/' + props.usession.session;

    fetch(datasrc)
      .then((res) => res.json())
      .then((data) =>
          setAppState({ loading: false, mydata: data })
      )

  }, [setAppState]);

  if (appState === null) {
    return (
      <h1>Not Ready</h1>
    );
  }

   const chartConfigs = {
    type: 'line',
    width: 600,
    height: 400,
    dataFormat: 'json',
    dataSource: appState.mydata
  };

  ReactFC.fcRoot(FusionCharts, Charts, FusionTheme);

  return (<ReactFC {...chartConfigs} />);

}

export default Graphbase;
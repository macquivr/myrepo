import React, { useEffect, useState } from 'react';

import DataTable from 'react-data-table-component';

function MData(props) {

  const [appState, setAppState] = useState({
    loading: false,
    mydata: null,
  });

  useEffect(() => {
    setAppState({ loading: true });
    const datasrc = '/SpringBootRepository/ledger/' + props.session.session;

    fetch(datasrc)
      .then((res) => res.json())
      .then((data) =>
         setAppState({ loading: false, mydata: data })
      )

  }, [setAppState]);

  var pagination = true;
  var perPage = 10;

  if (appState.mydata != null) {
    return (
      <DataTable
        title={props.title}
        columns={props.columns}
        data={appState.mydata[props.jsonp]}
        pagination={pagination}
        paginationPerPage={perPage}
      />
    );
  }

  return (
    <h1>Not Ready</h1>
  );
}

export default MData;
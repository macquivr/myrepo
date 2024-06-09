import React from 'react';

import { Route, Switch } from 'react-router'
import AppI from './AppI'
import RouteB from './components/RouteB'
import RouteWB from './components/RouteWB'

function Apps() {
   return (
    <Switch>
      <Route path="/" component={AppI} exact />
      <Route path="/home/:sessionId" component={RouteB} next />
      <Route path="/importw/:sessionId" component={RouteWB} next />
    </Switch>
  );
}
export default Apps;


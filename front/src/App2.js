import React from 'react';

import { Route, Switch } from 'react-router'
import AppI from './AppI'
import RouteB from './components/RouteB'

function Apps() {
   return (
    <Switch>
      <Route path="/" component={AppI} exact />
      <Route path="/home/:sessionId" component={RouteB} next />
    </Switch>
  );
}
export default Apps;


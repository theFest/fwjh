import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Sources from './sources';
import SourcesDetail from './sources-detail';
import SourcesUpdate from './sources-update';
import SourcesDeleteDialog from './sources-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SourcesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SourcesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SourcesDetail} />
      <ErrorBoundaryRoute path={match.url} component={Sources} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SourcesDeleteDialog} />
  </>
);

export default Routes;

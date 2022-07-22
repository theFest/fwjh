import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Projects from './projects';
import ProjectsDetail from './projects-detail';
import ProjectsUpdate from './projects-update';
import ProjectsDeleteDialog from './projects-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProjectsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProjectsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProjectsDetail} />
      <ErrorBoundaryRoute path={match.url} component={Projects} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProjectsDeleteDialog} />
  </>
);

export default Routes;

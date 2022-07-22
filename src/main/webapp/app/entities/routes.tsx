import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Topics from './topics';
import Posts from './posts';
import Projects from './projects';
import Sources from './sources';
import Tags from './tags';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}topics`} component={Topics} />
        <ErrorBoundaryRoute path={`${match.url}posts`} component={Posts} />
        <ErrorBoundaryRoute path={`${match.url}projects`} component={Projects} />
        <ErrorBoundaryRoute path={`${match.url}sources`} component={Sources} />
        <ErrorBoundaryRoute path={`${match.url}tags`} component={Tags} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};

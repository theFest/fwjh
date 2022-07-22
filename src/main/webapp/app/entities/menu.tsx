import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/topics">
        <Translate contentKey="global.menu.entities.topics" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/posts">
        <Translate contentKey="global.menu.entities.posts" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/projects">
        <Translate contentKey="global.menu.entities.projects" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/sources">
        <Translate contentKey="global.menu.entities.sources" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/tags">
        <Translate contentKey="global.menu.entities.tags" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;

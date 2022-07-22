import topics from 'app/entities/topics/topics.reducer';
import posts from 'app/entities/posts/posts.reducer';
import projects from 'app/entities/projects/projects.reducer';
import sources from 'app/entities/sources/sources.reducer';
import tags from 'app/entities/tags/tags.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  topics,
  posts,
  projects,
  sources,
  tags,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;

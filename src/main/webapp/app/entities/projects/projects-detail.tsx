import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './projects.reducer';

export const ProjectsDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const projectsEntity = useAppSelector(state => state.projects.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="projectsDetailsHeading">
          <Translate contentKey="fwjhApp.projects.detail.title">Projects</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{projectsEntity.id}</dd>
          <dt>
            <span id="project">
              <Translate contentKey="fwjhApp.projects.project">Project</Translate>
            </span>
          </dt>
          <dd>{projectsEntity.project}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="fwjhApp.projects.description">Description</Translate>
            </span>
          </dt>
          <dd>{projectsEntity.description}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="fwjhApp.projects.content">Content</Translate>
            </span>
          </dt>
          <dd>{projectsEntity.content}</dd>
          <dt>
            <span id="files">
              <Translate contentKey="fwjhApp.projects.files">Files</Translate>
            </span>
          </dt>
          <dd>
            {projectsEntity.files ? (
              <div>
                {projectsEntity.filesContentType ? (
                  <a onClick={openFile(projectsEntity.filesContentType, projectsEntity.files)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {projectsEntity.filesContentType}, {byteSize(projectsEntity.files)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="author">
              <Translate contentKey="fwjhApp.projects.author">Author</Translate>
            </span>
          </dt>
          <dd>{projectsEntity.author}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="fwjhApp.projects.date">Date</Translate>
            </span>
          </dt>
          <dd>{projectsEntity.date ? <TextFormat value={projectsEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="fwjhApp.projects.tags">Tags</Translate>
          </dt>
          <dd>
            {projectsEntity.tags
              ? projectsEntity.tags.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.name}</a>
                    {projectsEntity.tags && i === projectsEntity.tags.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/projects" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/projects/${projectsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProjectsDetail;

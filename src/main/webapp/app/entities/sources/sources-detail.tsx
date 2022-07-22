import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './sources.reducer';

export const SourcesDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const sourcesEntity = useAppSelector(state => state.sources.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="sourcesDetailsHeading">
          <Translate contentKey="fwjhApp.sources.detail.title">Sources</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{sourcesEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="fwjhApp.sources.name">Name</Translate>
            </span>
          </dt>
          <dd>{sourcesEntity.name}</dd>
          <dt>
            <span id="url">
              <Translate contentKey="fwjhApp.sources.url">Url</Translate>
            </span>
          </dt>
          <dd>{sourcesEntity.url}</dd>
          <dt>
            <span id="author">
              <Translate contentKey="fwjhApp.sources.author">Author</Translate>
            </span>
          </dt>
          <dd>{sourcesEntity.author}</dd>
          <dt>
            <span id="attachments">
              <Translate contentKey="fwjhApp.sources.attachments">Attachments</Translate>
            </span>
          </dt>
          <dd>
            {sourcesEntity.attachments ? (
              <div>
                {sourcesEntity.attachmentsContentType ? (
                  <a onClick={openFile(sourcesEntity.attachmentsContentType, sourcesEntity.attachments)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {sourcesEntity.attachmentsContentType}, {byteSize(sourcesEntity.attachments)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="date">
              <Translate contentKey="fwjhApp.sources.date">Date</Translate>
            </span>
          </dt>
          <dd>{sourcesEntity.date ? <TextFormat value={sourcesEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="fwjhApp.sources.tags">Tags</Translate>
          </dt>
          <dd>
            {sourcesEntity.tags
              ? sourcesEntity.tags.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.name}</a>
                    {sourcesEntity.tags && i === sourcesEntity.tags.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/sources" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sources/${sourcesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SourcesDetail;

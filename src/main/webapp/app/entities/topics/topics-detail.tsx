import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './topics.reducer';

export const TopicsDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const topicsEntity = useAppSelector(state => state.topics.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="topicsDetailsHeading">
          <Translate contentKey="fwjhApp.topics.detail.title">Topics</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{topicsEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="fwjhApp.topics.name">Name</Translate>
            </span>
          </dt>
          <dd>{topicsEntity.name}</dd>
          <dt>
            <span id="science">
              <Translate contentKey="fwjhApp.topics.science">Science</Translate>
            </span>
          </dt>
          <dd>{topicsEntity.science}</dd>
          <dt>
            <span id="information">
              <Translate contentKey="fwjhApp.topics.information">Information</Translate>
            </span>
          </dt>
          <dd>{topicsEntity.information}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="fwjhApp.topics.date">Date</Translate>
            </span>
          </dt>
          <dd>{topicsEntity.date ? <TextFormat value={topicsEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="fwjhApp.topics.user">User</Translate>
          </dt>
          <dd>{topicsEntity.user ? topicsEntity.user.login : ''}</dd>
          <dt>
            <Translate contentKey="fwjhApp.topics.tags">Tags</Translate>
          </dt>
          <dd>
            {topicsEntity.tags
              ? topicsEntity.tags.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.name}</a>
                    {topicsEntity.tags && i === topicsEntity.tags.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/topics" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/topics/${topicsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TopicsDetail;

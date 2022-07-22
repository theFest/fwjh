import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './posts.reducer';

export const PostsDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const postsEntity = useAppSelector(state => state.posts.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="postsDetailsHeading">
          <Translate contentKey="fwjhApp.posts.detail.title">Posts</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{postsEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="fwjhApp.posts.title">Title</Translate>
            </span>
          </dt>
          <dd>{postsEntity.title}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="fwjhApp.posts.content">Content</Translate>
            </span>
          </dt>
          <dd>{postsEntity.content}</dd>
          <dt>
            <span id="images">
              <Translate contentKey="fwjhApp.posts.images">Images</Translate>
            </span>
          </dt>
          <dd>
            {postsEntity.images ? (
              <div>
                {postsEntity.imagesContentType ? (
                  <a onClick={openFile(postsEntity.imagesContentType, postsEntity.images)}>
                    <img src={`data:${postsEntity.imagesContentType};base64,${postsEntity.images}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {postsEntity.imagesContentType}, {byteSize(postsEntity.images)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="additionalData">
              <Translate contentKey="fwjhApp.posts.additionalData">Additional Data</Translate>
            </span>
          </dt>
          <dd>
            {postsEntity.additionalData ? (
              <div>
                {postsEntity.additionalDataContentType ? (
                  <a onClick={openFile(postsEntity.additionalDataContentType, postsEntity.additionalData)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {postsEntity.additionalDataContentType}, {byteSize(postsEntity.additionalData)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="comments">
              <Translate contentKey="fwjhApp.posts.comments">Comments</Translate>
            </span>
          </dt>
          <dd>{postsEntity.comments}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="fwjhApp.posts.date">Date</Translate>
            </span>
          </dt>
          <dd>{postsEntity.date ? <TextFormat value={postsEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="fwjhApp.posts.topics">Topics</Translate>
          </dt>
          <dd>{postsEntity.topics ? postsEntity.topics.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/posts" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/posts/${postsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PostsDetail;

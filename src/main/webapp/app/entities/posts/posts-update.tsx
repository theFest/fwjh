import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITopics } from 'app/shared/model/topics.model';
import { getEntities as getTopics } from 'app/entities/topics/topics.reducer';
import { IPosts } from 'app/shared/model/posts.model';
import { getEntity, updateEntity, createEntity, reset } from './posts.reducer';

export const PostsUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const topics = useAppSelector(state => state.topics.entities);
  const postsEntity = useAppSelector(state => state.posts.entity);
  const loading = useAppSelector(state => state.posts.loading);
  const updating = useAppSelector(state => state.posts.updating);
  const updateSuccess = useAppSelector(state => state.posts.updateSuccess);
  const handleClose = () => {
    props.history.push('/posts');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getTopics({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...postsEntity,
      ...values,
      topics: topics.find(it => it.id.toString() === values.topics.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...postsEntity,
          topics: postsEntity?.topics?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="fwjhApp.posts.home.createOrEditLabel" data-cy="PostsCreateUpdateHeading">
            <Translate contentKey="fwjhApp.posts.home.createOrEditLabel">Create or edit a Posts</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="posts-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('fwjhApp.posts.title')}
                id="posts-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('fwjhApp.posts.content')}
                id="posts-content"
                name="content"
                data-cy="content"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedBlobField
                label={translate('fwjhApp.posts.images')}
                id="posts-images"
                name="images"
                data-cy="images"
                isImage
                accept="image/*"
              />
              <ValidatedBlobField
                label={translate('fwjhApp.posts.additionalData')}
                id="posts-additionalData"
                name="additionalData"
                data-cy="additionalData"
                openActionLabel={translate('entity.action.open')}
              />
              <ValidatedField
                label={translate('fwjhApp.posts.comments')}
                id="posts-comments"
                name="comments"
                data-cy="comments"
                type="textarea"
              />
              <ValidatedField
                label={translate('fwjhApp.posts.date')}
                id="posts-date"
                name="date"
                data-cy="date"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField id="posts-topics" name="topics" data-cy="topics" label={translate('fwjhApp.posts.topics')} type="select">
                <option value="" key="0" />
                {topics
                  ? topics.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/posts" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PostsUpdate;

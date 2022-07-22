package com.fwjh.app.repository;

import com.fwjh.app.domain.Topics;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class TopicsRepositoryWithBagRelationshipsImpl implements TopicsRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Topics> fetchBagRelationships(Optional<Topics> topics) {
        return topics.map(this::fetchTags);
    }

    @Override
    public Page<Topics> fetchBagRelationships(Page<Topics> topics) {
        return new PageImpl<>(fetchBagRelationships(topics.getContent()), topics.getPageable(), topics.getTotalElements());
    }

    @Override
    public List<Topics> fetchBagRelationships(List<Topics> topics) {
        return Optional.of(topics).map(this::fetchTags).orElse(Collections.emptyList());
    }

    Topics fetchTags(Topics result) {
        return entityManager
            .createQuery("select topics from Topics topics left join fetch topics.tags where topics is :topics", Topics.class)
            .setParameter("topics", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Topics> fetchTags(List<Topics> topics) {
        return entityManager
            .createQuery("select distinct topics from Topics topics left join fetch topics.tags where topics in :topics", Topics.class)
            .setParameter("topics", topics)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}

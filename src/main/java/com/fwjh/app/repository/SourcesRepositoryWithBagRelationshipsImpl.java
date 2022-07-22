package com.fwjh.app.repository;

import com.fwjh.app.domain.Sources;
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
public class SourcesRepositoryWithBagRelationshipsImpl implements SourcesRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Sources> fetchBagRelationships(Optional<Sources> sources) {
        return sources.map(this::fetchTags);
    }

    @Override
    public Page<Sources> fetchBagRelationships(Page<Sources> sources) {
        return new PageImpl<>(fetchBagRelationships(sources.getContent()), sources.getPageable(), sources.getTotalElements());
    }

    @Override
    public List<Sources> fetchBagRelationships(List<Sources> sources) {
        return Optional.of(sources).map(this::fetchTags).orElse(Collections.emptyList());
    }

    Sources fetchTags(Sources result) {
        return entityManager
            .createQuery("select sources from Sources sources left join fetch sources.tags where sources is :sources", Sources.class)
            .setParameter("sources", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Sources> fetchTags(List<Sources> sources) {
        return entityManager
            .createQuery(
                "select distinct sources from Sources sources left join fetch sources.tags where sources in :sources",
                Sources.class
            )
            .setParameter("sources", sources)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}

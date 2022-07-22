package com.fwjh.app.repository;

import com.fwjh.app.domain.Projects;
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
public class ProjectsRepositoryWithBagRelationshipsImpl implements ProjectsRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Projects> fetchBagRelationships(Optional<Projects> projects) {
        return projects.map(this::fetchTags);
    }

    @Override
    public Page<Projects> fetchBagRelationships(Page<Projects> projects) {
        return new PageImpl<>(fetchBagRelationships(projects.getContent()), projects.getPageable(), projects.getTotalElements());
    }

    @Override
    public List<Projects> fetchBagRelationships(List<Projects> projects) {
        return Optional.of(projects).map(this::fetchTags).orElse(Collections.emptyList());
    }

    Projects fetchTags(Projects result) {
        return entityManager
            .createQuery("select projects from Projects projects left join fetch projects.tags where projects is :projects", Projects.class)
            .setParameter("projects", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Projects> fetchTags(List<Projects> projects) {
        return entityManager
            .createQuery(
                "select distinct projects from Projects projects left join fetch projects.tags where projects in :projects",
                Projects.class
            )
            .setParameter("projects", projects)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}

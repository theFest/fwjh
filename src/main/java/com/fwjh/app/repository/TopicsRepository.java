package com.fwjh.app.repository;

import com.fwjh.app.domain.Topics;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Topics entity.
 */
@Repository
public interface TopicsRepository extends TopicsRepositoryWithBagRelationships, JpaRepository<Topics, Long> {
    @Query("select topics from Topics topics where topics.user.login = ?#{principal.username}")
    List<Topics> findByUserIsCurrentUser();

    default Optional<Topics> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Topics> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Topics> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct topics from Topics topics left join fetch topics.user",
        countQuery = "select count(distinct topics) from Topics topics"
    )
    Page<Topics> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct topics from Topics topics left join fetch topics.user")
    List<Topics> findAllWithToOneRelationships();

    @Query("select topics from Topics topics left join fetch topics.user where topics.id =:id")
    Optional<Topics> findOneWithToOneRelationships(@Param("id") Long id);
}

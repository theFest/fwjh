package com.fwjh.app.repository;

import com.fwjh.app.domain.Posts;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Posts entity.
 */
@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {
    default Optional<Posts> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Posts> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Posts> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct posts from Posts posts left join fetch posts.topics",
        countQuery = "select count(distinct posts) from Posts posts"
    )
    Page<Posts> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct posts from Posts posts left join fetch posts.topics")
    List<Posts> findAllWithToOneRelationships();

    @Query("select posts from Posts posts left join fetch posts.topics where posts.id =:id")
    Optional<Posts> findOneWithToOneRelationships(@Param("id") Long id);
}

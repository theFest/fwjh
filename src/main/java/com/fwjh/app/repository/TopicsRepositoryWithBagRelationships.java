package com.fwjh.app.repository;

import com.fwjh.app.domain.Topics;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface TopicsRepositoryWithBagRelationships {
    Optional<Topics> fetchBagRelationships(Optional<Topics> topics);

    List<Topics> fetchBagRelationships(List<Topics> topics);

    Page<Topics> fetchBagRelationships(Page<Topics> topics);
}

package com.fwjh.app.repository;

import com.fwjh.app.domain.Sources;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface SourcesRepositoryWithBagRelationships {
    Optional<Sources> fetchBagRelationships(Optional<Sources> sources);

    List<Sources> fetchBagRelationships(List<Sources> sources);

    Page<Sources> fetchBagRelationships(Page<Sources> sources);
}

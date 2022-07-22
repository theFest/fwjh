package com.fwjh.app.repository;

import com.fwjh.app.domain.Projects;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ProjectsRepositoryWithBagRelationships {
    Optional<Projects> fetchBagRelationships(Optional<Projects> projects);

    List<Projects> fetchBagRelationships(List<Projects> projects);

    Page<Projects> fetchBagRelationships(Page<Projects> projects);
}

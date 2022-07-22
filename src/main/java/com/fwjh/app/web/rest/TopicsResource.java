package com.fwjh.app.web.rest;

import com.fwjh.app.domain.Topics;
import com.fwjh.app.repository.TopicsRepository;
import com.fwjh.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.fwjh.app.domain.Topics}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TopicsResource {

    private final Logger log = LoggerFactory.getLogger(TopicsResource.class);

    private static final String ENTITY_NAME = "topics";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TopicsRepository topicsRepository;

    public TopicsResource(TopicsRepository topicsRepository) {
        this.topicsRepository = topicsRepository;
    }

    /**
     * {@code POST  /topics} : Create a new topics.
     *
     * @param topics the topics to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new topics, or with status {@code 400 (Bad Request)} if the topics has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/topics")
    public ResponseEntity<Topics> createTopics(@Valid @RequestBody Topics topics) throws URISyntaxException {
        log.debug("REST request to save Topics : {}", topics);
        if (topics.getId() != null) {
            throw new BadRequestAlertException("A new topics cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Topics result = topicsRepository.save(topics);
        return ResponseEntity
            .created(new URI("/api/topics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /topics/:id} : Updates an existing topics.
     *
     * @param id the id of the topics to save.
     * @param topics the topics to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated topics,
     * or with status {@code 400 (Bad Request)} if the topics is not valid,
     * or with status {@code 500 (Internal Server Error)} if the topics couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/topics/{id}")
    public ResponseEntity<Topics> updateTopics(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Topics topics
    ) throws URISyntaxException {
        log.debug("REST request to update Topics : {}, {}", id, topics);
        if (topics.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, topics.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!topicsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Topics result = topicsRepository.save(topics);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, topics.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /topics/:id} : Partial updates given fields of an existing topics, field will ignore if it is null
     *
     * @param id the id of the topics to save.
     * @param topics the topics to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated topics,
     * or with status {@code 400 (Bad Request)} if the topics is not valid,
     * or with status {@code 404 (Not Found)} if the topics is not found,
     * or with status {@code 500 (Internal Server Error)} if the topics couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/topics/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Topics> partialUpdateTopics(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Topics topics
    ) throws URISyntaxException {
        log.debug("REST request to partial update Topics partially : {}, {}", id, topics);
        if (topics.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, topics.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!topicsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Topics> result = topicsRepository
            .findById(topics.getId())
            .map(existingTopics -> {
                if (topics.getName() != null) {
                    existingTopics.setName(topics.getName());
                }
                if (topics.getScience() != null) {
                    existingTopics.setScience(topics.getScience());
                }
                if (topics.getInformation() != null) {
                    existingTopics.setInformation(topics.getInformation());
                }
                if (topics.getDate() != null) {
                    existingTopics.setDate(topics.getDate());
                }

                return existingTopics;
            })
            .map(topicsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, topics.getId().toString())
        );
    }

    /**
     * {@code GET  /topics} : get all the topics.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of topics in body.
     */
    @GetMapping("/topics")
    public ResponseEntity<List<Topics>> getAllTopics(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Topics");
        Page<Topics> page;
        if (eagerload) {
            page = topicsRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = topicsRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /topics/:id} : get the "id" topics.
     *
     * @param id the id of the topics to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the topics, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/topics/{id}")
    public ResponseEntity<Topics> getTopics(@PathVariable Long id) {
        log.debug("REST request to get Topics : {}", id);
        Optional<Topics> topics = topicsRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(topics);
    }

    /**
     * {@code DELETE  /topics/:id} : delete the "id" topics.
     *
     * @param id the id of the topics to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/topics/{id}")
    public ResponseEntity<Void> deleteTopics(@PathVariable Long id) {
        log.debug("REST request to delete Topics : {}", id);
        topicsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

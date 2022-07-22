package com.fwjh.app.web.rest;

import com.fwjh.app.domain.Sources;
import com.fwjh.app.repository.SourcesRepository;
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
 * REST controller for managing {@link com.fwjh.app.domain.Sources}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SourcesResource {

    private final Logger log = LoggerFactory.getLogger(SourcesResource.class);

    private static final String ENTITY_NAME = "sources";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SourcesRepository sourcesRepository;

    public SourcesResource(SourcesRepository sourcesRepository) {
        this.sourcesRepository = sourcesRepository;
    }

    /**
     * {@code POST  /sources} : Create a new sources.
     *
     * @param sources the sources to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sources, or with status {@code 400 (Bad Request)} if the sources has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sources")
    public ResponseEntity<Sources> createSources(@Valid @RequestBody Sources sources) throws URISyntaxException {
        log.debug("REST request to save Sources : {}", sources);
        if (sources.getId() != null) {
            throw new BadRequestAlertException("A new sources cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sources result = sourcesRepository.save(sources);
        return ResponseEntity
            .created(new URI("/api/sources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sources/:id} : Updates an existing sources.
     *
     * @param id the id of the sources to save.
     * @param sources the sources to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sources,
     * or with status {@code 400 (Bad Request)} if the sources is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sources couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sources/{id}")
    public ResponseEntity<Sources> updateSources(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Sources sources
    ) throws URISyntaxException {
        log.debug("REST request to update Sources : {}, {}", id, sources);
        if (sources.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sources.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sourcesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Sources result = sourcesRepository.save(sources);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sources.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sources/:id} : Partial updates given fields of an existing sources, field will ignore if it is null
     *
     * @param id the id of the sources to save.
     * @param sources the sources to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sources,
     * or with status {@code 400 (Bad Request)} if the sources is not valid,
     * or with status {@code 404 (Not Found)} if the sources is not found,
     * or with status {@code 500 (Internal Server Error)} if the sources couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sources/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Sources> partialUpdateSources(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Sources sources
    ) throws URISyntaxException {
        log.debug("REST request to partial update Sources partially : {}, {}", id, sources);
        if (sources.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sources.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sourcesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Sources> result = sourcesRepository
            .findById(sources.getId())
            .map(existingSources -> {
                if (sources.getName() != null) {
                    existingSources.setName(sources.getName());
                }
                if (sources.getUrl() != null) {
                    existingSources.setUrl(sources.getUrl());
                }
                if (sources.getAuthor() != null) {
                    existingSources.setAuthor(sources.getAuthor());
                }
                if (sources.getAttachments() != null) {
                    existingSources.setAttachments(sources.getAttachments());
                }
                if (sources.getAttachmentsContentType() != null) {
                    existingSources.setAttachmentsContentType(sources.getAttachmentsContentType());
                }
                if (sources.getDate() != null) {
                    existingSources.setDate(sources.getDate());
                }

                return existingSources;
            })
            .map(sourcesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sources.getId().toString())
        );
    }

    /**
     * {@code GET  /sources} : get all the sources.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sources in body.
     */
    @GetMapping("/sources")
    public ResponseEntity<List<Sources>> getAllSources(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Sources");
        Page<Sources> page;
        if (eagerload) {
            page = sourcesRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = sourcesRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sources/:id} : get the "id" sources.
     *
     * @param id the id of the sources to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sources, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sources/{id}")
    public ResponseEntity<Sources> getSources(@PathVariable Long id) {
        log.debug("REST request to get Sources : {}", id);
        Optional<Sources> sources = sourcesRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(sources);
    }

    /**
     * {@code DELETE  /sources/:id} : delete the "id" sources.
     *
     * @param id the id of the sources to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sources/{id}")
    public ResponseEntity<Void> deleteSources(@PathVariable Long id) {
        log.debug("REST request to delete Sources : {}", id);
        sourcesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

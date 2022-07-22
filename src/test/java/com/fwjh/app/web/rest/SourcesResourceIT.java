package com.fwjh.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fwjh.app.IntegrationTest;
import com.fwjh.app.domain.Sources;
import com.fwjh.app.repository.SourcesRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link SourcesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SourcesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    private static final byte[] DEFAULT_ATTACHMENTS = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ATTACHMENTS = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ATTACHMENTS_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ATTACHMENTS_CONTENT_TYPE = "image/png";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/sources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SourcesRepository sourcesRepository;

    @Mock
    private SourcesRepository sourcesRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSourcesMockMvc;

    private Sources sources;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sources createEntity(EntityManager em) {
        Sources sources = new Sources()
            .name(DEFAULT_NAME)
            .url(DEFAULT_URL)
            .author(DEFAULT_AUTHOR)
            .attachments(DEFAULT_ATTACHMENTS)
            .attachmentsContentType(DEFAULT_ATTACHMENTS_CONTENT_TYPE)
            .date(DEFAULT_DATE);
        return sources;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sources createUpdatedEntity(EntityManager em) {
        Sources sources = new Sources()
            .name(UPDATED_NAME)
            .url(UPDATED_URL)
            .author(UPDATED_AUTHOR)
            .attachments(UPDATED_ATTACHMENTS)
            .attachmentsContentType(UPDATED_ATTACHMENTS_CONTENT_TYPE)
            .date(UPDATED_DATE);
        return sources;
    }

    @BeforeEach
    public void initTest() {
        sources = createEntity(em);
    }

    @Test
    @Transactional
    void createSources() throws Exception {
        int databaseSizeBeforeCreate = sourcesRepository.findAll().size();
        // Create the Sources
        restSourcesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sources)))
            .andExpect(status().isCreated());

        // Validate the Sources in the database
        List<Sources> sourcesList = sourcesRepository.findAll();
        assertThat(sourcesList).hasSize(databaseSizeBeforeCreate + 1);
        Sources testSources = sourcesList.get(sourcesList.size() - 1);
        assertThat(testSources.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSources.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testSources.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testSources.getAttachments()).isEqualTo(DEFAULT_ATTACHMENTS);
        assertThat(testSources.getAttachmentsContentType()).isEqualTo(DEFAULT_ATTACHMENTS_CONTENT_TYPE);
        assertThat(testSources.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createSourcesWithExistingId() throws Exception {
        // Create the Sources with an existing ID
        sources.setId(1L);

        int databaseSizeBeforeCreate = sourcesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSourcesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sources)))
            .andExpect(status().isBadRequest());

        // Validate the Sources in the database
        List<Sources> sourcesList = sourcesRepository.findAll();
        assertThat(sourcesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = sourcesRepository.findAll().size();
        // set the field null
        sources.setName(null);

        // Create the Sources, which fails.

        restSourcesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sources)))
            .andExpect(status().isBadRequest());

        List<Sources> sourcesList = sourcesRepository.findAll();
        assertThat(sourcesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAuthorIsRequired() throws Exception {
        int databaseSizeBeforeTest = sourcesRepository.findAll().size();
        // set the field null
        sources.setAuthor(null);

        // Create the Sources, which fails.

        restSourcesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sources)))
            .andExpect(status().isBadRequest());

        List<Sources> sourcesList = sourcesRepository.findAll();
        assertThat(sourcesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = sourcesRepository.findAll().size();
        // set the field null
        sources.setDate(null);

        // Create the Sources, which fails.

        restSourcesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sources)))
            .andExpect(status().isBadRequest());

        List<Sources> sourcesList = sourcesRepository.findAll();
        assertThat(sourcesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSources() throws Exception {
        // Initialize the database
        sourcesRepository.saveAndFlush(sources);

        // Get all the sourcesList
        restSourcesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sources.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
            .andExpect(jsonPath("$.[*].attachmentsContentType").value(hasItem(DEFAULT_ATTACHMENTS_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].attachments").value(hasItem(Base64Utils.encodeToString(DEFAULT_ATTACHMENTS))))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSourcesWithEagerRelationshipsIsEnabled() throws Exception {
        when(sourcesRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSourcesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(sourcesRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSourcesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(sourcesRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSourcesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(sourcesRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSources() throws Exception {
        // Initialize the database
        sourcesRepository.saveAndFlush(sources);

        // Get the sources
        restSourcesMockMvc
            .perform(get(ENTITY_API_URL_ID, sources.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sources.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR))
            .andExpect(jsonPath("$.attachmentsContentType").value(DEFAULT_ATTACHMENTS_CONTENT_TYPE))
            .andExpect(jsonPath("$.attachments").value(Base64Utils.encodeToString(DEFAULT_ATTACHMENTS)))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSources() throws Exception {
        // Get the sources
        restSourcesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSources() throws Exception {
        // Initialize the database
        sourcesRepository.saveAndFlush(sources);

        int databaseSizeBeforeUpdate = sourcesRepository.findAll().size();

        // Update the sources
        Sources updatedSources = sourcesRepository.findById(sources.getId()).get();
        // Disconnect from session so that the updates on updatedSources are not directly saved in db
        em.detach(updatedSources);
        updatedSources
            .name(UPDATED_NAME)
            .url(UPDATED_URL)
            .author(UPDATED_AUTHOR)
            .attachments(UPDATED_ATTACHMENTS)
            .attachmentsContentType(UPDATED_ATTACHMENTS_CONTENT_TYPE)
            .date(UPDATED_DATE);

        restSourcesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSources.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSources))
            )
            .andExpect(status().isOk());

        // Validate the Sources in the database
        List<Sources> sourcesList = sourcesRepository.findAll();
        assertThat(sourcesList).hasSize(databaseSizeBeforeUpdate);
        Sources testSources = sourcesList.get(sourcesList.size() - 1);
        assertThat(testSources.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSources.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testSources.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testSources.getAttachments()).isEqualTo(UPDATED_ATTACHMENTS);
        assertThat(testSources.getAttachmentsContentType()).isEqualTo(UPDATED_ATTACHMENTS_CONTENT_TYPE);
        assertThat(testSources.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSources() throws Exception {
        int databaseSizeBeforeUpdate = sourcesRepository.findAll().size();
        sources.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourcesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sources.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sources))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sources in the database
        List<Sources> sourcesList = sourcesRepository.findAll();
        assertThat(sourcesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSources() throws Exception {
        int databaseSizeBeforeUpdate = sourcesRepository.findAll().size();
        sources.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourcesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sources))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sources in the database
        List<Sources> sourcesList = sourcesRepository.findAll();
        assertThat(sourcesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSources() throws Exception {
        int databaseSizeBeforeUpdate = sourcesRepository.findAll().size();
        sources.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourcesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sources)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sources in the database
        List<Sources> sourcesList = sourcesRepository.findAll();
        assertThat(sourcesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSourcesWithPatch() throws Exception {
        // Initialize the database
        sourcesRepository.saveAndFlush(sources);

        int databaseSizeBeforeUpdate = sourcesRepository.findAll().size();

        // Update the sources using partial update
        Sources partialUpdatedSources = new Sources();
        partialUpdatedSources.setId(sources.getId());

        partialUpdatedSources
            .name(UPDATED_NAME)
            .author(UPDATED_AUTHOR)
            .attachments(UPDATED_ATTACHMENTS)
            .attachmentsContentType(UPDATED_ATTACHMENTS_CONTENT_TYPE);

        restSourcesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSources.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSources))
            )
            .andExpect(status().isOk());

        // Validate the Sources in the database
        List<Sources> sourcesList = sourcesRepository.findAll();
        assertThat(sourcesList).hasSize(databaseSizeBeforeUpdate);
        Sources testSources = sourcesList.get(sourcesList.size() - 1);
        assertThat(testSources.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSources.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testSources.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testSources.getAttachments()).isEqualTo(UPDATED_ATTACHMENTS);
        assertThat(testSources.getAttachmentsContentType()).isEqualTo(UPDATED_ATTACHMENTS_CONTENT_TYPE);
        assertThat(testSources.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSourcesWithPatch() throws Exception {
        // Initialize the database
        sourcesRepository.saveAndFlush(sources);

        int databaseSizeBeforeUpdate = sourcesRepository.findAll().size();

        // Update the sources using partial update
        Sources partialUpdatedSources = new Sources();
        partialUpdatedSources.setId(sources.getId());

        partialUpdatedSources
            .name(UPDATED_NAME)
            .url(UPDATED_URL)
            .author(UPDATED_AUTHOR)
            .attachments(UPDATED_ATTACHMENTS)
            .attachmentsContentType(UPDATED_ATTACHMENTS_CONTENT_TYPE)
            .date(UPDATED_DATE);

        restSourcesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSources.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSources))
            )
            .andExpect(status().isOk());

        // Validate the Sources in the database
        List<Sources> sourcesList = sourcesRepository.findAll();
        assertThat(sourcesList).hasSize(databaseSizeBeforeUpdate);
        Sources testSources = sourcesList.get(sourcesList.size() - 1);
        assertThat(testSources.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSources.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testSources.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testSources.getAttachments()).isEqualTo(UPDATED_ATTACHMENTS);
        assertThat(testSources.getAttachmentsContentType()).isEqualTo(UPDATED_ATTACHMENTS_CONTENT_TYPE);
        assertThat(testSources.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSources() throws Exception {
        int databaseSizeBeforeUpdate = sourcesRepository.findAll().size();
        sources.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourcesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sources.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sources))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sources in the database
        List<Sources> sourcesList = sourcesRepository.findAll();
        assertThat(sourcesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSources() throws Exception {
        int databaseSizeBeforeUpdate = sourcesRepository.findAll().size();
        sources.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourcesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sources))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sources in the database
        List<Sources> sourcesList = sourcesRepository.findAll();
        assertThat(sourcesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSources() throws Exception {
        int databaseSizeBeforeUpdate = sourcesRepository.findAll().size();
        sources.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourcesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sources)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sources in the database
        List<Sources> sourcesList = sourcesRepository.findAll();
        assertThat(sourcesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSources() throws Exception {
        // Initialize the database
        sourcesRepository.saveAndFlush(sources);

        int databaseSizeBeforeDelete = sourcesRepository.findAll().size();

        // Delete the sources
        restSourcesMockMvc
            .perform(delete(ENTITY_API_URL_ID, sources.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sources> sourcesList = sourcesRepository.findAll();
        assertThat(sourcesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

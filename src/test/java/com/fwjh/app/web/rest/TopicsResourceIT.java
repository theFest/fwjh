package com.fwjh.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fwjh.app.IntegrationTest;
import com.fwjh.app.domain.Topics;
import com.fwjh.app.repository.TopicsRepository;
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
 * Integration tests for the {@link TopicsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TopicsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SCIENCE = "AAAAAAAAAA";
    private static final String UPDATED_SCIENCE = "BBBBBBBBBB";

    private static final String DEFAULT_INFORMATION = "AAAAAAAAAA";
    private static final String UPDATED_INFORMATION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/topics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TopicsRepository topicsRepository;

    @Mock
    private TopicsRepository topicsRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTopicsMockMvc;

    private Topics topics;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Topics createEntity(EntityManager em) {
        Topics topics = new Topics().name(DEFAULT_NAME).science(DEFAULT_SCIENCE).information(DEFAULT_INFORMATION).date(DEFAULT_DATE);
        return topics;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Topics createUpdatedEntity(EntityManager em) {
        Topics topics = new Topics().name(UPDATED_NAME).science(UPDATED_SCIENCE).information(UPDATED_INFORMATION).date(UPDATED_DATE);
        return topics;
    }

    @BeforeEach
    public void initTest() {
        topics = createEntity(em);
    }

    @Test
    @Transactional
    void createTopics() throws Exception {
        int databaseSizeBeforeCreate = topicsRepository.findAll().size();
        // Create the Topics
        restTopicsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(topics)))
            .andExpect(status().isCreated());

        // Validate the Topics in the database
        List<Topics> topicsList = topicsRepository.findAll();
        assertThat(topicsList).hasSize(databaseSizeBeforeCreate + 1);
        Topics testTopics = topicsList.get(topicsList.size() - 1);
        assertThat(testTopics.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTopics.getScience()).isEqualTo(DEFAULT_SCIENCE);
        assertThat(testTopics.getInformation()).isEqualTo(DEFAULT_INFORMATION);
        assertThat(testTopics.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createTopicsWithExistingId() throws Exception {
        // Create the Topics with an existing ID
        topics.setId(1L);

        int databaseSizeBeforeCreate = topicsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTopicsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(topics)))
            .andExpect(status().isBadRequest());

        // Validate the Topics in the database
        List<Topics> topicsList = topicsRepository.findAll();
        assertThat(topicsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = topicsRepository.findAll().size();
        // set the field null
        topics.setName(null);

        // Create the Topics, which fails.

        restTopicsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(topics)))
            .andExpect(status().isBadRequest());

        List<Topics> topicsList = topicsRepository.findAll();
        assertThat(topicsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = topicsRepository.findAll().size();
        // set the field null
        topics.setDate(null);

        // Create the Topics, which fails.

        restTopicsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(topics)))
            .andExpect(status().isBadRequest());

        List<Topics> topicsList = topicsRepository.findAll();
        assertThat(topicsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTopics() throws Exception {
        // Initialize the database
        topicsRepository.saveAndFlush(topics);

        // Get all the topicsList
        restTopicsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(topics.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].science").value(hasItem(DEFAULT_SCIENCE)))
            .andExpect(jsonPath("$.[*].information").value(hasItem(DEFAULT_INFORMATION.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTopicsWithEagerRelationshipsIsEnabled() throws Exception {
        when(topicsRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTopicsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(topicsRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTopicsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(topicsRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTopicsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(topicsRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTopics() throws Exception {
        // Initialize the database
        topicsRepository.saveAndFlush(topics);

        // Get the topics
        restTopicsMockMvc
            .perform(get(ENTITY_API_URL_ID, topics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(topics.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.science").value(DEFAULT_SCIENCE))
            .andExpect(jsonPath("$.information").value(DEFAULT_INFORMATION.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTopics() throws Exception {
        // Get the topics
        restTopicsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTopics() throws Exception {
        // Initialize the database
        topicsRepository.saveAndFlush(topics);

        int databaseSizeBeforeUpdate = topicsRepository.findAll().size();

        // Update the topics
        Topics updatedTopics = topicsRepository.findById(topics.getId()).get();
        // Disconnect from session so that the updates on updatedTopics are not directly saved in db
        em.detach(updatedTopics);
        updatedTopics.name(UPDATED_NAME).science(UPDATED_SCIENCE).information(UPDATED_INFORMATION).date(UPDATED_DATE);

        restTopicsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTopics.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTopics))
            )
            .andExpect(status().isOk());

        // Validate the Topics in the database
        List<Topics> topicsList = topicsRepository.findAll();
        assertThat(topicsList).hasSize(databaseSizeBeforeUpdate);
        Topics testTopics = topicsList.get(topicsList.size() - 1);
        assertThat(testTopics.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTopics.getScience()).isEqualTo(UPDATED_SCIENCE);
        assertThat(testTopics.getInformation()).isEqualTo(UPDATED_INFORMATION);
        assertThat(testTopics.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingTopics() throws Exception {
        int databaseSizeBeforeUpdate = topicsRepository.findAll().size();
        topics.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTopicsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, topics.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(topics))
            )
            .andExpect(status().isBadRequest());

        // Validate the Topics in the database
        List<Topics> topicsList = topicsRepository.findAll();
        assertThat(topicsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTopics() throws Exception {
        int databaseSizeBeforeUpdate = topicsRepository.findAll().size();
        topics.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTopicsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(topics))
            )
            .andExpect(status().isBadRequest());

        // Validate the Topics in the database
        List<Topics> topicsList = topicsRepository.findAll();
        assertThat(topicsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTopics() throws Exception {
        int databaseSizeBeforeUpdate = topicsRepository.findAll().size();
        topics.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTopicsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(topics)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Topics in the database
        List<Topics> topicsList = topicsRepository.findAll();
        assertThat(topicsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTopicsWithPatch() throws Exception {
        // Initialize the database
        topicsRepository.saveAndFlush(topics);

        int databaseSizeBeforeUpdate = topicsRepository.findAll().size();

        // Update the topics using partial update
        Topics partialUpdatedTopics = new Topics();
        partialUpdatedTopics.setId(topics.getId());

        partialUpdatedTopics.date(UPDATED_DATE);

        restTopicsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTopics.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTopics))
            )
            .andExpect(status().isOk());

        // Validate the Topics in the database
        List<Topics> topicsList = topicsRepository.findAll();
        assertThat(topicsList).hasSize(databaseSizeBeforeUpdate);
        Topics testTopics = topicsList.get(topicsList.size() - 1);
        assertThat(testTopics.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTopics.getScience()).isEqualTo(DEFAULT_SCIENCE);
        assertThat(testTopics.getInformation()).isEqualTo(DEFAULT_INFORMATION);
        assertThat(testTopics.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateTopicsWithPatch() throws Exception {
        // Initialize the database
        topicsRepository.saveAndFlush(topics);

        int databaseSizeBeforeUpdate = topicsRepository.findAll().size();

        // Update the topics using partial update
        Topics partialUpdatedTopics = new Topics();
        partialUpdatedTopics.setId(topics.getId());

        partialUpdatedTopics.name(UPDATED_NAME).science(UPDATED_SCIENCE).information(UPDATED_INFORMATION).date(UPDATED_DATE);

        restTopicsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTopics.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTopics))
            )
            .andExpect(status().isOk());

        // Validate the Topics in the database
        List<Topics> topicsList = topicsRepository.findAll();
        assertThat(topicsList).hasSize(databaseSizeBeforeUpdate);
        Topics testTopics = topicsList.get(topicsList.size() - 1);
        assertThat(testTopics.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTopics.getScience()).isEqualTo(UPDATED_SCIENCE);
        assertThat(testTopics.getInformation()).isEqualTo(UPDATED_INFORMATION);
        assertThat(testTopics.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingTopics() throws Exception {
        int databaseSizeBeforeUpdate = topicsRepository.findAll().size();
        topics.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTopicsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, topics.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(topics))
            )
            .andExpect(status().isBadRequest());

        // Validate the Topics in the database
        List<Topics> topicsList = topicsRepository.findAll();
        assertThat(topicsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTopics() throws Exception {
        int databaseSizeBeforeUpdate = topicsRepository.findAll().size();
        topics.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTopicsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(topics))
            )
            .andExpect(status().isBadRequest());

        // Validate the Topics in the database
        List<Topics> topicsList = topicsRepository.findAll();
        assertThat(topicsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTopics() throws Exception {
        int databaseSizeBeforeUpdate = topicsRepository.findAll().size();
        topics.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTopicsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(topics)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Topics in the database
        List<Topics> topicsList = topicsRepository.findAll();
        assertThat(topicsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTopics() throws Exception {
        // Initialize the database
        topicsRepository.saveAndFlush(topics);

        int databaseSizeBeforeDelete = topicsRepository.findAll().size();

        // Delete the topics
        restTopicsMockMvc
            .perform(delete(ENTITY_API_URL_ID, topics.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Topics> topicsList = topicsRepository.findAll();
        assertThat(topicsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

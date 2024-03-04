package com.reclamation.pfe.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reclamation.pfe.IntegrationTest;
import com.reclamation.pfe.domain.PieceJointe;
import com.reclamation.pfe.domain.Reclamation;
import com.reclamation.pfe.repository.PieceJointeRepository;
import com.reclamation.pfe.service.dto.PieceJointeDTO;
import com.reclamation.pfe.service.mapper.PieceJointeMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PieceJointeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PieceJointeResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_DATA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/piece-jointes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PieceJointeRepository pieceJointeRepository;

    @Autowired
    private PieceJointeMapper pieceJointeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPieceJointeMockMvc;

    private PieceJointe pieceJointe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PieceJointe createEntity(EntityManager em) {
        PieceJointe pieceJointe = new PieceJointe().url(DEFAULT_URL).type(DEFAULT_TYPE).data(DEFAULT_DATA);
        return pieceJointe;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PieceJointe createUpdatedEntity(EntityManager em) {
        PieceJointe pieceJointe = new PieceJointe().url(UPDATED_URL).type(UPDATED_TYPE).data(UPDATED_DATA);
        return pieceJointe;
    }

    @BeforeEach
    public void initTest() {
        pieceJointe = createEntity(em);
    }

    @Test
    @Transactional
    void createPieceJointe() throws Exception {
        int databaseSizeBeforeCreate = pieceJointeRepository.findAll().size();
        // Create the PieceJointe
        PieceJointeDTO pieceJointeDTO = pieceJointeMapper.toDto(pieceJointe);
        restPieceJointeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pieceJointeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeCreate + 1);
        PieceJointe testPieceJointe = pieceJointeList.get(pieceJointeList.size() - 1);
        assertThat(testPieceJointe.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testPieceJointe.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPieceJointe.getData()).isEqualTo(DEFAULT_DATA);
    }

    @Test
    @Transactional
    void createPieceJointeWithExistingId() throws Exception {
        // Create the PieceJointe with an existing ID
        pieceJointe.setId(1L);
        PieceJointeDTO pieceJointeDTO = pieceJointeMapper.toDto(pieceJointe);

        int databaseSizeBeforeCreate = pieceJointeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPieceJointeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pieceJointeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPieceJointes() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        // Get all the pieceJointeList
        restPieceJointeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pieceJointe.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA)));
    }

    @Test
    @Transactional
    void getPieceJointe() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        // Get the pieceJointe
        restPieceJointeMockMvc
            .perform(get(ENTITY_API_URL_ID, pieceJointe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pieceJointe.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA));
    }

    @Test
    @Transactional
    void getPieceJointesByIdFiltering() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        Long id = pieceJointe.getId();

        defaultPieceJointeShouldBeFound("id.equals=" + id);
        defaultPieceJointeShouldNotBeFound("id.notEquals=" + id);

        defaultPieceJointeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPieceJointeShouldNotBeFound("id.greaterThan=" + id);

        defaultPieceJointeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPieceJointeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPieceJointesByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        // Get all the pieceJointeList where url equals to DEFAULT_URL
        defaultPieceJointeShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the pieceJointeList where url equals to UPDATED_URL
        defaultPieceJointeShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllPieceJointesByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        // Get all the pieceJointeList where url in DEFAULT_URL or UPDATED_URL
        defaultPieceJointeShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the pieceJointeList where url equals to UPDATED_URL
        defaultPieceJointeShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllPieceJointesByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        // Get all the pieceJointeList where url is not null
        defaultPieceJointeShouldBeFound("url.specified=true");

        // Get all the pieceJointeList where url is null
        defaultPieceJointeShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    void getAllPieceJointesByUrlContainsSomething() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        // Get all the pieceJointeList where url contains DEFAULT_URL
        defaultPieceJointeShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the pieceJointeList where url contains UPDATED_URL
        defaultPieceJointeShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllPieceJointesByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        // Get all the pieceJointeList where url does not contain DEFAULT_URL
        defaultPieceJointeShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the pieceJointeList where url does not contain UPDATED_URL
        defaultPieceJointeShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllPieceJointesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        // Get all the pieceJointeList where type equals to DEFAULT_TYPE
        defaultPieceJointeShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the pieceJointeList where type equals to UPDATED_TYPE
        defaultPieceJointeShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPieceJointesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        // Get all the pieceJointeList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultPieceJointeShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the pieceJointeList where type equals to UPDATED_TYPE
        defaultPieceJointeShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPieceJointesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        // Get all the pieceJointeList where type is not null
        defaultPieceJointeShouldBeFound("type.specified=true");

        // Get all the pieceJointeList where type is null
        defaultPieceJointeShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllPieceJointesByTypeContainsSomething() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        // Get all the pieceJointeList where type contains DEFAULT_TYPE
        defaultPieceJointeShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the pieceJointeList where type contains UPDATED_TYPE
        defaultPieceJointeShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPieceJointesByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        // Get all the pieceJointeList where type does not contain DEFAULT_TYPE
        defaultPieceJointeShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the pieceJointeList where type does not contain UPDATED_TYPE
        defaultPieceJointeShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPieceJointesByDataIsEqualToSomething() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        // Get all the pieceJointeList where data equals to DEFAULT_DATA
        defaultPieceJointeShouldBeFound("data.equals=" + DEFAULT_DATA);

        // Get all the pieceJointeList where data equals to UPDATED_DATA
        defaultPieceJointeShouldNotBeFound("data.equals=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllPieceJointesByDataIsInShouldWork() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        // Get all the pieceJointeList where data in DEFAULT_DATA or UPDATED_DATA
        defaultPieceJointeShouldBeFound("data.in=" + DEFAULT_DATA + "," + UPDATED_DATA);

        // Get all the pieceJointeList where data equals to UPDATED_DATA
        defaultPieceJointeShouldNotBeFound("data.in=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllPieceJointesByDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        // Get all the pieceJointeList where data is not null
        defaultPieceJointeShouldBeFound("data.specified=true");

        // Get all the pieceJointeList where data is null
        defaultPieceJointeShouldNotBeFound("data.specified=false");
    }

    @Test
    @Transactional
    void getAllPieceJointesByDataContainsSomething() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        // Get all the pieceJointeList where data contains DEFAULT_DATA
        defaultPieceJointeShouldBeFound("data.contains=" + DEFAULT_DATA);

        // Get all the pieceJointeList where data contains UPDATED_DATA
        defaultPieceJointeShouldNotBeFound("data.contains=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllPieceJointesByDataNotContainsSomething() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        // Get all the pieceJointeList where data does not contain DEFAULT_DATA
        defaultPieceJointeShouldNotBeFound("data.doesNotContain=" + DEFAULT_DATA);

        // Get all the pieceJointeList where data does not contain UPDATED_DATA
        defaultPieceJointeShouldBeFound("data.doesNotContain=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllPieceJointesByReclamationIsEqualToSomething() throws Exception {
        Reclamation reclamation;
        if (TestUtil.findAll(em, Reclamation.class).isEmpty()) {
            pieceJointeRepository.saveAndFlush(pieceJointe);
            reclamation = ReclamationResourceIT.createEntity(em);
        } else {
            reclamation = TestUtil.findAll(em, Reclamation.class).get(0);
        }
        em.persist(reclamation);
        em.flush();
        pieceJointe.setReclamation(reclamation);
        pieceJointeRepository.saveAndFlush(pieceJointe);
        Long reclamationId = reclamation.getId();
        // Get all the pieceJointeList where reclamation equals to reclamationId
        defaultPieceJointeShouldBeFound("reclamationId.equals=" + reclamationId);

        // Get all the pieceJointeList where reclamation equals to (reclamationId + 1)
        defaultPieceJointeShouldNotBeFound("reclamationId.equals=" + (reclamationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPieceJointeShouldBeFound(String filter) throws Exception {
        restPieceJointeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pieceJointe.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA)));

        // Check, that the count call also returns 1
        restPieceJointeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPieceJointeShouldNotBeFound(String filter) throws Exception {
        restPieceJointeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPieceJointeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPieceJointe() throws Exception {
        // Get the pieceJointe
        restPieceJointeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPieceJointe() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();

        // Update the pieceJointe
        PieceJointe updatedPieceJointe = pieceJointeRepository.findById(pieceJointe.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPieceJointe are not directly saved in db
        em.detach(updatedPieceJointe);
        updatedPieceJointe.url(UPDATED_URL).type(UPDATED_TYPE).data(UPDATED_DATA);
        PieceJointeDTO pieceJointeDTO = pieceJointeMapper.toDto(updatedPieceJointe);

        restPieceJointeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pieceJointeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pieceJointeDTO))
            )
            .andExpect(status().isOk());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
        PieceJointe testPieceJointe = pieceJointeList.get(pieceJointeList.size() - 1);
        assertThat(testPieceJointe.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testPieceJointe.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPieceJointe.getData()).isEqualTo(UPDATED_DATA);
    }

    @Test
    @Transactional
    void putNonExistingPieceJointe() throws Exception {
        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();
        pieceJointe.setId(longCount.incrementAndGet());

        // Create the PieceJointe
        PieceJointeDTO pieceJointeDTO = pieceJointeMapper.toDto(pieceJointe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPieceJointeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pieceJointeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pieceJointeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPieceJointe() throws Exception {
        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();
        pieceJointe.setId(longCount.incrementAndGet());

        // Create the PieceJointe
        PieceJointeDTO pieceJointeDTO = pieceJointeMapper.toDto(pieceJointe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPieceJointeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pieceJointeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPieceJointe() throws Exception {
        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();
        pieceJointe.setId(longCount.incrementAndGet());

        // Create the PieceJointe
        PieceJointeDTO pieceJointeDTO = pieceJointeMapper.toDto(pieceJointe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPieceJointeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pieceJointeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePieceJointeWithPatch() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();

        // Update the pieceJointe using partial update
        PieceJointe partialUpdatedPieceJointe = new PieceJointe();
        partialUpdatedPieceJointe.setId(pieceJointe.getId());

        partialUpdatedPieceJointe.type(UPDATED_TYPE);

        restPieceJointeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPieceJointe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPieceJointe))
            )
            .andExpect(status().isOk());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
        PieceJointe testPieceJointe = pieceJointeList.get(pieceJointeList.size() - 1);
        assertThat(testPieceJointe.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testPieceJointe.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPieceJointe.getData()).isEqualTo(DEFAULT_DATA);
    }

    @Test
    @Transactional
    void fullUpdatePieceJointeWithPatch() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();

        // Update the pieceJointe using partial update
        PieceJointe partialUpdatedPieceJointe = new PieceJointe();
        partialUpdatedPieceJointe.setId(pieceJointe.getId());

        partialUpdatedPieceJointe.url(UPDATED_URL).type(UPDATED_TYPE).data(UPDATED_DATA);

        restPieceJointeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPieceJointe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPieceJointe))
            )
            .andExpect(status().isOk());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
        PieceJointe testPieceJointe = pieceJointeList.get(pieceJointeList.size() - 1);
        assertThat(testPieceJointe.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testPieceJointe.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPieceJointe.getData()).isEqualTo(UPDATED_DATA);
    }

    @Test
    @Transactional
    void patchNonExistingPieceJointe() throws Exception {
        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();
        pieceJointe.setId(longCount.incrementAndGet());

        // Create the PieceJointe
        PieceJointeDTO pieceJointeDTO = pieceJointeMapper.toDto(pieceJointe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPieceJointeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pieceJointeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pieceJointeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPieceJointe() throws Exception {
        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();
        pieceJointe.setId(longCount.incrementAndGet());

        // Create the PieceJointe
        PieceJointeDTO pieceJointeDTO = pieceJointeMapper.toDto(pieceJointe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPieceJointeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pieceJointeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPieceJointe() throws Exception {
        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();
        pieceJointe.setId(longCount.incrementAndGet());

        // Create the PieceJointe
        PieceJointeDTO pieceJointeDTO = pieceJointeMapper.toDto(pieceJointe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPieceJointeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pieceJointeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePieceJointe() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        int databaseSizeBeforeDelete = pieceJointeRepository.findAll().size();

        // Delete the pieceJointe
        restPieceJointeMockMvc
            .perform(delete(ENTITY_API_URL_ID, pieceJointe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

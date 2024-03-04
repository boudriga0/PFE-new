package com.reclamation.pfe.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reclamation.pfe.IntegrationTest;
import com.reclamation.pfe.domain.Commentaire;
import com.reclamation.pfe.domain.Reclamation;
import com.reclamation.pfe.repository.CommentaireRepository;
import com.reclamation.pfe.service.dto.CommentaireDTO;
import com.reclamation.pfe.service.mapper.CommentaireMapper;
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
 * Integration tests for the {@link CommentaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommentaireResourceIT {

    private static final String DEFAULT_CONTENU = "AAAAAAAAAA";
    private static final String UPDATED_CONTENU = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/commentaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommentaireRepository commentaireRepository;

    @Autowired
    private CommentaireMapper commentaireMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommentaireMockMvc;

    private Commentaire commentaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commentaire createEntity(EntityManager em) {
        Commentaire commentaire = new Commentaire().contenu(DEFAULT_CONTENU);
        return commentaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commentaire createUpdatedEntity(EntityManager em) {
        Commentaire commentaire = new Commentaire().contenu(UPDATED_CONTENU);
        return commentaire;
    }

    @BeforeEach
    public void initTest() {
        commentaire = createEntity(em);
    }

    @Test
    @Transactional
    void createCommentaire() throws Exception {
        int databaseSizeBeforeCreate = commentaireRepository.findAll().size();
        // Create the Commentaire
        CommentaireDTO commentaireDTO = commentaireMapper.toDto(commentaire);
        restCommentaireMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentaireDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Commentaire in the database
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeCreate + 1);
        Commentaire testCommentaire = commentaireList.get(commentaireList.size() - 1);
        assertThat(testCommentaire.getContenu()).isEqualTo(DEFAULT_CONTENU);
    }

    @Test
    @Transactional
    void createCommentaireWithExistingId() throws Exception {
        // Create the Commentaire with an existing ID
        commentaire.setId(1L);
        CommentaireDTO commentaireDTO = commentaireMapper.toDto(commentaire);

        int databaseSizeBeforeCreate = commentaireRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommentaireMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commentaire in the database
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCommentaires() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList
        restCommentaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].contenu").value(hasItem(DEFAULT_CONTENU)));
    }

    @Test
    @Transactional
    void getCommentaire() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get the commentaire
        restCommentaireMockMvc
            .perform(get(ENTITY_API_URL_ID, commentaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commentaire.getId().intValue()))
            .andExpect(jsonPath("$.contenu").value(DEFAULT_CONTENU));
    }

    @Test
    @Transactional
    void getCommentairesByIdFiltering() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        Long id = commentaire.getId();

        defaultCommentaireShouldBeFound("id.equals=" + id);
        defaultCommentaireShouldNotBeFound("id.notEquals=" + id);

        defaultCommentaireShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCommentaireShouldNotBeFound("id.greaterThan=" + id);

        defaultCommentaireShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCommentaireShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCommentairesByContenuIsEqualToSomething() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where contenu equals to DEFAULT_CONTENU
        defaultCommentaireShouldBeFound("contenu.equals=" + DEFAULT_CONTENU);

        // Get all the commentaireList where contenu equals to UPDATED_CONTENU
        defaultCommentaireShouldNotBeFound("contenu.equals=" + UPDATED_CONTENU);
    }

    @Test
    @Transactional
    void getAllCommentairesByContenuIsInShouldWork() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where contenu in DEFAULT_CONTENU or UPDATED_CONTENU
        defaultCommentaireShouldBeFound("contenu.in=" + DEFAULT_CONTENU + "," + UPDATED_CONTENU);

        // Get all the commentaireList where contenu equals to UPDATED_CONTENU
        defaultCommentaireShouldNotBeFound("contenu.in=" + UPDATED_CONTENU);
    }

    @Test
    @Transactional
    void getAllCommentairesByContenuIsNullOrNotNull() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where contenu is not null
        defaultCommentaireShouldBeFound("contenu.specified=true");

        // Get all the commentaireList where contenu is null
        defaultCommentaireShouldNotBeFound("contenu.specified=false");
    }

    @Test
    @Transactional
    void getAllCommentairesByContenuContainsSomething() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where contenu contains DEFAULT_CONTENU
        defaultCommentaireShouldBeFound("contenu.contains=" + DEFAULT_CONTENU);

        // Get all the commentaireList where contenu contains UPDATED_CONTENU
        defaultCommentaireShouldNotBeFound("contenu.contains=" + UPDATED_CONTENU);
    }

    @Test
    @Transactional
    void getAllCommentairesByContenuNotContainsSomething() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where contenu does not contain DEFAULT_CONTENU
        defaultCommentaireShouldNotBeFound("contenu.doesNotContain=" + DEFAULT_CONTENU);

        // Get all the commentaireList where contenu does not contain UPDATED_CONTENU
        defaultCommentaireShouldBeFound("contenu.doesNotContain=" + UPDATED_CONTENU);
    }

    @Test
    @Transactional
    void getAllCommentairesByReclamationIsEqualToSomething() throws Exception {
        Reclamation reclamation;
        if (TestUtil.findAll(em, Reclamation.class).isEmpty()) {
            commentaireRepository.saveAndFlush(commentaire);
            reclamation = ReclamationResourceIT.createEntity(em);
        } else {
            reclamation = TestUtil.findAll(em, Reclamation.class).get(0);
        }
        em.persist(reclamation);
        em.flush();
        commentaire.setReclamation(reclamation);
        commentaireRepository.saveAndFlush(commentaire);
        Long reclamationId = reclamation.getId();
        // Get all the commentaireList where reclamation equals to reclamationId
        defaultCommentaireShouldBeFound("reclamationId.equals=" + reclamationId);

        // Get all the commentaireList where reclamation equals to (reclamationId + 1)
        defaultCommentaireShouldNotBeFound("reclamationId.equals=" + (reclamationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommentaireShouldBeFound(String filter) throws Exception {
        restCommentaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].contenu").value(hasItem(DEFAULT_CONTENU)));

        // Check, that the count call also returns 1
        restCommentaireMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommentaireShouldNotBeFound(String filter) throws Exception {
        restCommentaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommentaireMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCommentaire() throws Exception {
        // Get the commentaire
        restCommentaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCommentaire() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        int databaseSizeBeforeUpdate = commentaireRepository.findAll().size();

        // Update the commentaire
        Commentaire updatedCommentaire = commentaireRepository.findById(commentaire.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCommentaire are not directly saved in db
        em.detach(updatedCommentaire);
        updatedCommentaire.contenu(UPDATED_CONTENU);
        CommentaireDTO commentaireDTO = commentaireMapper.toDto(updatedCommentaire);

        restCommentaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commentaireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentaireDTO))
            )
            .andExpect(status().isOk());

        // Validate the Commentaire in the database
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeUpdate);
        Commentaire testCommentaire = commentaireList.get(commentaireList.size() - 1);
        assertThat(testCommentaire.getContenu()).isEqualTo(UPDATED_CONTENU);
    }

    @Test
    @Transactional
    void putNonExistingCommentaire() throws Exception {
        int databaseSizeBeforeUpdate = commentaireRepository.findAll().size();
        commentaire.setId(longCount.incrementAndGet());

        // Create the Commentaire
        CommentaireDTO commentaireDTO = commentaireMapper.toDto(commentaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commentaireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commentaire in the database
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommentaire() throws Exception {
        int databaseSizeBeforeUpdate = commentaireRepository.findAll().size();
        commentaire.setId(longCount.incrementAndGet());

        // Create the Commentaire
        CommentaireDTO commentaireDTO = commentaireMapper.toDto(commentaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commentaire in the database
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommentaire() throws Exception {
        int databaseSizeBeforeUpdate = commentaireRepository.findAll().size();
        commentaire.setId(longCount.incrementAndGet());

        // Create the Commentaire
        CommentaireDTO commentaireDTO = commentaireMapper.toDto(commentaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentaireMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentaireDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commentaire in the database
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommentaireWithPatch() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        int databaseSizeBeforeUpdate = commentaireRepository.findAll().size();

        // Update the commentaire using partial update
        Commentaire partialUpdatedCommentaire = new Commentaire();
        partialUpdatedCommentaire.setId(commentaire.getId());

        restCommentaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommentaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommentaire))
            )
            .andExpect(status().isOk());

        // Validate the Commentaire in the database
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeUpdate);
        Commentaire testCommentaire = commentaireList.get(commentaireList.size() - 1);
        assertThat(testCommentaire.getContenu()).isEqualTo(DEFAULT_CONTENU);
    }

    @Test
    @Transactional
    void fullUpdateCommentaireWithPatch() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        int databaseSizeBeforeUpdate = commentaireRepository.findAll().size();

        // Update the commentaire using partial update
        Commentaire partialUpdatedCommentaire = new Commentaire();
        partialUpdatedCommentaire.setId(commentaire.getId());

        partialUpdatedCommentaire.contenu(UPDATED_CONTENU);

        restCommentaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommentaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommentaire))
            )
            .andExpect(status().isOk());

        // Validate the Commentaire in the database
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeUpdate);
        Commentaire testCommentaire = commentaireList.get(commentaireList.size() - 1);
        assertThat(testCommentaire.getContenu()).isEqualTo(UPDATED_CONTENU);
    }

    @Test
    @Transactional
    void patchNonExistingCommentaire() throws Exception {
        int databaseSizeBeforeUpdate = commentaireRepository.findAll().size();
        commentaire.setId(longCount.incrementAndGet());

        // Create the Commentaire
        CommentaireDTO commentaireDTO = commentaireMapper.toDto(commentaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commentaireDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commentaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commentaire in the database
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommentaire() throws Exception {
        int databaseSizeBeforeUpdate = commentaireRepository.findAll().size();
        commentaire.setId(longCount.incrementAndGet());

        // Create the Commentaire
        CommentaireDTO commentaireDTO = commentaireMapper.toDto(commentaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commentaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commentaire in the database
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommentaire() throws Exception {
        int databaseSizeBeforeUpdate = commentaireRepository.findAll().size();
        commentaire.setId(longCount.incrementAndGet());

        // Create the Commentaire
        CommentaireDTO commentaireDTO = commentaireMapper.toDto(commentaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentaireMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(commentaireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commentaire in the database
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommentaire() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        int databaseSizeBeforeDelete = commentaireRepository.findAll().size();

        // Delete the commentaire
        restCommentaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, commentaire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

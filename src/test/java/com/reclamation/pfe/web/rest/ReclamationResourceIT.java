package com.reclamation.pfe.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reclamation.pfe.IntegrationTest;
import com.reclamation.pfe.domain.Commentaire;
import com.reclamation.pfe.domain.Personne;
import com.reclamation.pfe.domain.PieceJointe;
import com.reclamation.pfe.domain.Reclamation;
import com.reclamation.pfe.repository.ReclamationRepository;
import com.reclamation.pfe.service.dto.ReclamationDTO;
import com.reclamation.pfe.service.mapper.ReclamationMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ReclamationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReclamationResourceIT {

    private static final String DEFAULT_CATEGORIE = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORIE = "BBBBBBBBBB";


    private static final String DEFAULT_ETAT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/reclamations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReclamationRepository reclamationRepository;

    @Autowired
    private ReclamationMapper reclamationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReclamationMockMvc;

    private Reclamation reclamation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reclamation createEntity(EntityManager em) {
        Reclamation reclamation = new Reclamation()
            .categorie(DEFAULT_CATEGORIE)
            .etat(DEFAULT_ETAT)
            .numero(DEFAULT_NUMERO)
            .date(DEFAULT_DATE);
        return reclamation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reclamation createUpdatedEntity(EntityManager em) {
        Reclamation reclamation = new Reclamation()
            .categorie(UPDATED_CATEGORIE)
            .etat(UPDATED_ETAT)
            .numero(UPDATED_NUMERO)
            .date(UPDATED_DATE);
        return reclamation;
    }

    @BeforeEach
    public void initTest() {
        reclamation = createEntity(em);
    }

    @Test
    @Transactional
    void createReclamation() throws Exception {
        int databaseSizeBeforeCreate = reclamationRepository.findAll().size();
        // Create the Reclamation
        ReclamationDTO reclamationDTO = reclamationMapper.toDto(reclamation);
        restReclamationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reclamationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeCreate + 1);
        Reclamation testReclamation = reclamationList.get(reclamationList.size() - 1);
        assertThat(testReclamation.getCategorie()).isEqualTo(DEFAULT_CATEGORIE);
        assertThat(testReclamation.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testReclamation.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testReclamation.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createReclamationWithExistingId() throws Exception {
        // Create the Reclamation with an existing ID
        reclamation.setId(1L);
        ReclamationDTO reclamationDTO = reclamationMapper.toDto(reclamation);

        int databaseSizeBeforeCreate = reclamationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReclamationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reclamationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReclamations() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList
        restReclamationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reclamation.getId().intValue())))
            .andExpect(jsonPath("$.[*].categorie").value(hasItem(DEFAULT_CATEGORIE)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getReclamation() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get the reclamation
        restReclamationMockMvc
            .perform(get(ENTITY_API_URL_ID, reclamation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reclamation.getId().intValue()))
            .andExpect(jsonPath("$.categorie").value(DEFAULT_CATEGORIE))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getReclamationsByIdFiltering() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        Long id = reclamation.getId();

        defaultReclamationShouldBeFound("id.equals=" + id);
        defaultReclamationShouldNotBeFound("id.notEquals=" + id);

        defaultReclamationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultReclamationShouldNotBeFound("id.greaterThan=" + id);

        defaultReclamationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultReclamationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReclamationsByCategorieIsEqualToSomething() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where categorie equals to DEFAULT_CATEGORIE
        defaultReclamationShouldBeFound("categorie.equals=" + DEFAULT_CATEGORIE);

        // Get all the reclamationList where categorie equals to UPDATED_CATEGORIE
        defaultReclamationShouldNotBeFound("categorie.equals=" + UPDATED_CATEGORIE);
    }

    @Test
    @Transactional
    void getAllReclamationsByCategorieIsInShouldWork() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where categorie in DEFAULT_CATEGORIE or UPDATED_CATEGORIE
        defaultReclamationShouldBeFound("categorie.in=" + DEFAULT_CATEGORIE + "," + UPDATED_CATEGORIE);

        // Get all the reclamationList where categorie equals to UPDATED_CATEGORIE
        defaultReclamationShouldNotBeFound("categorie.in=" + UPDATED_CATEGORIE);
    }

    @Test
    @Transactional
    void getAllReclamationsByCategorieIsNullOrNotNull() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where categorie is not null
        defaultReclamationShouldBeFound("categorie.specified=true");

        // Get all the reclamationList where categorie is null
        defaultReclamationShouldNotBeFound("categorie.specified=false");
    }

    @Test
    @Transactional
    void getAllReclamationsByCategorieContainsSomething() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where categorie contains DEFAULT_CATEGORIE
        defaultReclamationShouldBeFound("categorie.contains=" + DEFAULT_CATEGORIE);

        // Get all the reclamationList where categorie contains UPDATED_CATEGORIE
        defaultReclamationShouldNotBeFound("categorie.contains=" + UPDATED_CATEGORIE);
    }

    @Test
    @Transactional
    void getAllReclamationsByCategorieNotContainsSomething() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where categorie does not contain DEFAULT_CATEGORIE
        defaultReclamationShouldNotBeFound("categorie.doesNotContain=" + DEFAULT_CATEGORIE);

        // Get all the reclamationList where categorie does not contain UPDATED_CATEGORIE
        defaultReclamationShouldBeFound("categorie.doesNotContain=" + UPDATED_CATEGORIE);
    }

    @Test
    @Transactional
    void getAllReclamationsByEtatIsEqualToSomething() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where etat equals to DEFAULT_ETAT
        defaultReclamationShouldBeFound("etat.equals=" + DEFAULT_ETAT);

        // Get all the reclamationList where etat equals to UPDATED_ETAT
        defaultReclamationShouldNotBeFound("etat.equals=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllReclamationsByEtatIsInShouldWork() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where etat in DEFAULT_ETAT or UPDATED_ETAT
        defaultReclamationShouldBeFound("etat.in=" + DEFAULT_ETAT + "," + UPDATED_ETAT);

        // Get all the reclamationList where etat equals to UPDATED_ETAT
        defaultReclamationShouldNotBeFound("etat.in=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllReclamationsByEtatIsNullOrNotNull() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where etat is not null
        defaultReclamationShouldBeFound("etat.specified=true");

        // Get all the reclamationList where etat is null
        defaultReclamationShouldNotBeFound("etat.specified=false");
    }

    @Test
    @Transactional
    void getAllReclamationsByEtatContainsSomething() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where etat contains DEFAULT_ETAT
        defaultReclamationShouldBeFound("etat.contains=" + DEFAULT_ETAT);

        // Get all the reclamationList where etat contains UPDATED_ETAT
        defaultReclamationShouldNotBeFound("etat.contains=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllReclamationsByEtatNotContainsSomething() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where etat does not contain DEFAULT_ETAT
        defaultReclamationShouldNotBeFound("etat.doesNotContain=" + DEFAULT_ETAT);

        // Get all the reclamationList where etat does not contain UPDATED_ETAT
        defaultReclamationShouldBeFound("etat.doesNotContain=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllReclamationsByNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where numero equals to DEFAULT_NUMERO
        defaultReclamationShouldBeFound("numero.equals=" + DEFAULT_NUMERO);

        // Get all the reclamationList where numero equals to UPDATED_NUMERO
        defaultReclamationShouldNotBeFound("numero.equals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllReclamationsByNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where numero in DEFAULT_NUMERO or UPDATED_NUMERO
        defaultReclamationShouldBeFound("numero.in=" + DEFAULT_NUMERO + "," + UPDATED_NUMERO);

        // Get all the reclamationList where numero equals to UPDATED_NUMERO
        defaultReclamationShouldNotBeFound("numero.in=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllReclamationsByNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where numero is not null
        defaultReclamationShouldBeFound("numero.specified=true");

        // Get all the reclamationList where numero is null
        defaultReclamationShouldNotBeFound("numero.specified=false");
    }

    @Test
    @Transactional
    void getAllReclamationsByNumeroContainsSomething() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where numero contains DEFAULT_NUMERO
        defaultReclamationShouldBeFound("numero.contains=" + DEFAULT_NUMERO);

        // Get all the reclamationList where numero contains UPDATED_NUMERO
        defaultReclamationShouldNotBeFound("numero.contains=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllReclamationsByNumeroNotContainsSomething() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where numero does not contain DEFAULT_NUMERO
        defaultReclamationShouldNotBeFound("numero.doesNotContain=" + DEFAULT_NUMERO);

        // Get all the reclamationList where numero does not contain UPDATED_NUMERO
        defaultReclamationShouldBeFound("numero.doesNotContain=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllReclamationsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where date equals to DEFAULT_DATE
        defaultReclamationShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the reclamationList where date equals to UPDATED_DATE
        defaultReclamationShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllReclamationsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where date in DEFAULT_DATE or UPDATED_DATE
        defaultReclamationShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the reclamationList where date equals to UPDATED_DATE
        defaultReclamationShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllReclamationsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where date is not null
        defaultReclamationShouldBeFound("date.specified=true");

        // Get all the reclamationList where date is null
        defaultReclamationShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllReclamationsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where date is greater than or equal to DEFAULT_DATE
        defaultReclamationShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the reclamationList where date is greater than or equal to UPDATED_DATE
        defaultReclamationShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllReclamationsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where date is less than or equal to DEFAULT_DATE
        defaultReclamationShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the reclamationList where date is less than or equal to SMALLER_DATE
        defaultReclamationShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllReclamationsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where date is less than DEFAULT_DATE
        defaultReclamationShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the reclamationList where date is less than UPDATED_DATE
        defaultReclamationShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllReclamationsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where date is greater than DEFAULT_DATE
        defaultReclamationShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the reclamationList where date is greater than SMALLER_DATE
        defaultReclamationShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllReclamationsByPieceJointeIsEqualToSomething() throws Exception {
        PieceJointe pieceJointe;
        if (TestUtil.findAll(em, PieceJointe.class).isEmpty()) {
            reclamationRepository.saveAndFlush(reclamation);
            pieceJointe = PieceJointeResourceIT.createEntity(em);
        } else {
            pieceJointe = TestUtil.findAll(em, PieceJointe.class).get(0);
        }
        em.persist(pieceJointe);
        em.flush();
        reclamation.addPieceJointe(pieceJointe);
        reclamationRepository.saveAndFlush(reclamation);
        Long pieceJointeId = pieceJointe.getId();
        // Get all the reclamationList where pieceJointe equals to pieceJointeId
        defaultReclamationShouldBeFound("pieceJointeId.equals=" + pieceJointeId);

        // Get all the reclamationList where pieceJointe equals to (pieceJointeId + 1)
        defaultReclamationShouldNotBeFound("pieceJointeId.equals=" + (pieceJointeId + 1));
    }

    @Test
    @Transactional
    void getAllReclamationsByCommentaireIsEqualToSomething() throws Exception {
        Commentaire commentaire;
        if (TestUtil.findAll(em, Commentaire.class).isEmpty()) {
            reclamationRepository.saveAndFlush(reclamation);
            commentaire = CommentaireResourceIT.createEntity(em);
        } else {
            commentaire = TestUtil.findAll(em, Commentaire.class).get(0);
        }
        em.persist(commentaire);
        em.flush();
        reclamation.addCommentaire(commentaire);
        reclamationRepository.saveAndFlush(reclamation);
        Long commentaireId = commentaire.getId();
        // Get all the reclamationList where commentaire equals to commentaireId
        defaultReclamationShouldBeFound("commentaireId.equals=" + commentaireId);

        // Get all the reclamationList where commentaire equals to (commentaireId + 1)
        defaultReclamationShouldNotBeFound("commentaireId.equals=" + (commentaireId + 1));
    }

    @Test
    @Transactional
    void getAllReclamationsByPersonneIsEqualToSomething() throws Exception {
        Personne personne;
        if (TestUtil.findAll(em, Personne.class).isEmpty()) {
            reclamationRepository.saveAndFlush(reclamation);
            personne = PersonneResourceIT.createEntity(em);
        } else {
            personne = TestUtil.findAll(em, Personne.class).get(0);
        }
        em.persist(personne);
        em.flush();
        reclamation.setPersonne(personne);
        reclamationRepository.saveAndFlush(reclamation);
        Long personneId = personne.getId();
        // Get all the reclamationList where personne equals to personneId
        defaultReclamationShouldBeFound("personneId.equals=" + personneId);

        // Get all the reclamationList where personne equals to (personneId + 1)
        defaultReclamationShouldNotBeFound("personneId.equals=" + (personneId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReclamationShouldBeFound(String filter) throws Exception {
        restReclamationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reclamation.getId().intValue())))
            .andExpect(jsonPath("$.[*].categorie").value(hasItem(DEFAULT_CATEGORIE)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restReclamationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReclamationShouldNotBeFound(String filter) throws Exception {
        restReclamationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReclamationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReclamation() throws Exception {
        // Get the reclamation
        restReclamationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReclamation() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();

        // Update the reclamation
        Reclamation updatedReclamation = reclamationRepository.findById(reclamation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReclamation are not directly saved in db
        em.detach(updatedReclamation);
        updatedReclamation.categorie(UPDATED_CATEGORIE).etat(UPDATED_ETAT).numero(UPDATED_NUMERO).date(UPDATED_DATE);
        ReclamationDTO reclamationDTO = reclamationMapper.toDto(updatedReclamation);

        restReclamationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reclamationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reclamationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);
        Reclamation testReclamation = reclamationList.get(reclamationList.size() - 1);
        assertThat(testReclamation.getCategorie()).isEqualTo(UPDATED_CATEGORIE);
        assertThat(testReclamation.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testReclamation.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testReclamation.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingReclamation() throws Exception {
        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();
        reclamation.setId(longCount.incrementAndGet());

        // Create the Reclamation
        ReclamationDTO reclamationDTO = reclamationMapper.toDto(reclamation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reclamationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reclamationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReclamation() throws Exception {
        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();
        reclamation.setId(longCount.incrementAndGet());

        // Create the Reclamation
        ReclamationDTO reclamationDTO = reclamationMapper.toDto(reclamation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reclamationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReclamation() throws Exception {
        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();
        reclamation.setId(longCount.incrementAndGet());

        // Create the Reclamation
        ReclamationDTO reclamationDTO = reclamationMapper.toDto(reclamation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reclamationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReclamationWithPatch() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();

        // Update the reclamation using partial update
        Reclamation partialUpdatedReclamation = new Reclamation();
        partialUpdatedReclamation.setId(reclamation.getId());

        partialUpdatedReclamation.categorie(UPDATED_CATEGORIE).date(UPDATED_DATE);

        restReclamationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReclamation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReclamation))
            )
            .andExpect(status().isOk());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);
        Reclamation testReclamation = reclamationList.get(reclamationList.size() - 1);
        assertThat(testReclamation.getCategorie()).isEqualTo(UPDATED_CATEGORIE);
        assertThat(testReclamation.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testReclamation.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testReclamation.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateReclamationWithPatch() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();

        // Update the reclamation using partial update
        Reclamation partialUpdatedReclamation = new Reclamation();
        partialUpdatedReclamation.setId(reclamation.getId());

        partialUpdatedReclamation.categorie(UPDATED_CATEGORIE).etat(UPDATED_ETAT).numero(UPDATED_NUMERO).date(UPDATED_DATE);

        restReclamationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReclamation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReclamation))
            )
            .andExpect(status().isOk());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);
        Reclamation testReclamation = reclamationList.get(reclamationList.size() - 1);
        assertThat(testReclamation.getCategorie()).isEqualTo(UPDATED_CATEGORIE);
        assertThat(testReclamation.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testReclamation.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testReclamation.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingReclamation() throws Exception {
        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();
        reclamation.setId(longCount.incrementAndGet());

        // Create the Reclamation
        ReclamationDTO reclamationDTO = reclamationMapper.toDto(reclamation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reclamationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reclamationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReclamation() throws Exception {
        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();
        reclamation.setId(longCount.incrementAndGet());

        // Create the Reclamation
        ReclamationDTO reclamationDTO = reclamationMapper.toDto(reclamation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reclamationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReclamation() throws Exception {
        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();
        reclamation.setId(longCount.incrementAndGet());

        // Create the Reclamation
        ReclamationDTO reclamationDTO = reclamationMapper.toDto(reclamation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reclamationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReclamation() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        int databaseSizeBeforeDelete = reclamationRepository.findAll().size();

        // Delete the reclamation
        restReclamationMockMvc
            .perform(delete(ENTITY_API_URL_ID, reclamation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

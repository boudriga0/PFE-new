package com.reclamation.pfe.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reclamation.pfe.IntegrationTest;
import com.reclamation.pfe.domain.Personne;
import com.reclamation.pfe.domain.Reclamation;
import com.reclamation.pfe.repository.PersonneRepository;
import com.reclamation.pfe.service.dto.PersonneDTO;
import com.reclamation.pfe.service.mapper.PersonneMapper;
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
 * Integration tests for the {@link PersonneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonneResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_C_IN = "AAAAAAAAAA";
    private static final String UPDATED_C_IN = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_NAISSANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAISSANCE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_NAISSANCE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_RIB = "AAAAAAAAAA";
    private static final String UPDATED_RIB = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_SEX = "AAAAAAAAAA";
    private static final String UPDATED_SEX = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/personnes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonneRepository personneRepository;

    @Autowired
    private PersonneMapper personneMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonneMockMvc;

    private Personne personne;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personne createEntity(EntityManager em) {
        Personne personne = new Personne()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .cIN(DEFAULT_C_IN)
            .dateNaissance(DEFAULT_DATE_NAISSANCE)
            .phone(DEFAULT_PHONE)
            .rib(DEFAULT_RIB)
            .email(DEFAULT_EMAIL)
            .sex(DEFAULT_SEX);
        return personne;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personne createUpdatedEntity(EntityManager em) {
        Personne personne = new Personne()
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .cIN(UPDATED_C_IN)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .phone(UPDATED_PHONE)
            .rib(UPDATED_RIB)
            .email(UPDATED_EMAIL)
            .sex(UPDATED_SEX);
        return personne;
    }

    @BeforeEach
    public void initTest() {
        personne = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonne() throws Exception {
        int databaseSizeBeforeCreate = personneRepository.findAll().size();
        // Create the Personne
        PersonneDTO personneDTO = personneMapper.toDto(personne);
        restPersonneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personneDTO)))
            .andExpect(status().isCreated());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeCreate + 1);
        Personne testPersonne = personneList.get(personneList.size() - 1);
        assertThat(testPersonne.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testPersonne.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testPersonne.getcIN()).isEqualTo(DEFAULT_C_IN);
        assertThat(testPersonne.getDateNaissance()).isEqualTo(DEFAULT_DATE_NAISSANCE);
        assertThat(testPersonne.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testPersonne.getRib()).isEqualTo(DEFAULT_RIB);
        assertThat(testPersonne.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPersonne.getSex()).isEqualTo(DEFAULT_SEX);
    }

    @Test
    @Transactional
    void createPersonneWithExistingId() throws Exception {
        // Create the Personne with an existing ID
        personne.setId(1L);
        PersonneDTO personneDTO = personneMapper.toDto(personne);

        int databaseSizeBeforeCreate = personneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personneDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPersonnes() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList
        restPersonneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personne.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].cIN").value(hasItem(DEFAULT_C_IN)))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].rib").value(hasItem(DEFAULT_RIB)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX)));
    }

    @Test
    @Transactional
    void getPersonne() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get the personne
        restPersonneMockMvc
            .perform(get(ENTITY_API_URL_ID, personne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personne.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.cIN").value(DEFAULT_C_IN))
            .andExpect(jsonPath("$.dateNaissance").value(DEFAULT_DATE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.rib").value(DEFAULT_RIB))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX));
    }

    @Test
    @Transactional
    void getPersonnesByIdFiltering() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        Long id = personne.getId();

        defaultPersonneShouldBeFound("id.equals=" + id);
        defaultPersonneShouldNotBeFound("id.notEquals=" + id);

        defaultPersonneShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPersonneShouldNotBeFound("id.greaterThan=" + id);

        defaultPersonneShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPersonneShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPersonnesByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where nom equals to DEFAULT_NOM
        defaultPersonneShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the personneList where nom equals to UPDATED_NOM
        defaultPersonneShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllPersonnesByNomIsInShouldWork() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultPersonneShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the personneList where nom equals to UPDATED_NOM
        defaultPersonneShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllPersonnesByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where nom is not null
        defaultPersonneShouldBeFound("nom.specified=true");

        // Get all the personneList where nom is null
        defaultPersonneShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonnesByNomContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where nom contains DEFAULT_NOM
        defaultPersonneShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the personneList where nom contains UPDATED_NOM
        defaultPersonneShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllPersonnesByNomNotContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where nom does not contain DEFAULT_NOM
        defaultPersonneShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the personneList where nom does not contain UPDATED_NOM
        defaultPersonneShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllPersonnesByPrenomIsEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where prenom equals to DEFAULT_PRENOM
        defaultPersonneShouldBeFound("prenom.equals=" + DEFAULT_PRENOM);

        // Get all the personneList where prenom equals to UPDATED_PRENOM
        defaultPersonneShouldNotBeFound("prenom.equals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllPersonnesByPrenomIsInShouldWork() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where prenom in DEFAULT_PRENOM or UPDATED_PRENOM
        defaultPersonneShouldBeFound("prenom.in=" + DEFAULT_PRENOM + "," + UPDATED_PRENOM);

        // Get all the personneList where prenom equals to UPDATED_PRENOM
        defaultPersonneShouldNotBeFound("prenom.in=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllPersonnesByPrenomIsNullOrNotNull() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where prenom is not null
        defaultPersonneShouldBeFound("prenom.specified=true");

        // Get all the personneList where prenom is null
        defaultPersonneShouldNotBeFound("prenom.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonnesByPrenomContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where prenom contains DEFAULT_PRENOM
        defaultPersonneShouldBeFound("prenom.contains=" + DEFAULT_PRENOM);

        // Get all the personneList where prenom contains UPDATED_PRENOM
        defaultPersonneShouldNotBeFound("prenom.contains=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllPersonnesByPrenomNotContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where prenom does not contain DEFAULT_PRENOM
        defaultPersonneShouldNotBeFound("prenom.doesNotContain=" + DEFAULT_PRENOM);

        // Get all the personneList where prenom does not contain UPDATED_PRENOM
        defaultPersonneShouldBeFound("prenom.doesNotContain=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllPersonnesBycINIsEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where cIN equals to DEFAULT_C_IN
        defaultPersonneShouldBeFound("cIN.equals=" + DEFAULT_C_IN);

        // Get all the personneList where cIN equals to UPDATED_C_IN
        defaultPersonneShouldNotBeFound("cIN.equals=" + UPDATED_C_IN);
    }

    @Test
    @Transactional
    void getAllPersonnesBycINIsInShouldWork() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where cIN in DEFAULT_C_IN or UPDATED_C_IN
        defaultPersonneShouldBeFound("cIN.in=" + DEFAULT_C_IN + "," + UPDATED_C_IN);

        // Get all the personneList where cIN equals to UPDATED_C_IN
        defaultPersonneShouldNotBeFound("cIN.in=" + UPDATED_C_IN);
    }

    @Test
    @Transactional
    void getAllPersonnesBycINIsNullOrNotNull() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where cIN is not null
        defaultPersonneShouldBeFound("cIN.specified=true");

        // Get all the personneList where cIN is null
        defaultPersonneShouldNotBeFound("cIN.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonnesBycINContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where cIN contains DEFAULT_C_IN
        defaultPersonneShouldBeFound("cIN.contains=" + DEFAULT_C_IN);

        // Get all the personneList where cIN contains UPDATED_C_IN
        defaultPersonneShouldNotBeFound("cIN.contains=" + UPDATED_C_IN);
    }

    @Test
    @Transactional
    void getAllPersonnesBycINNotContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where cIN does not contain DEFAULT_C_IN
        defaultPersonneShouldNotBeFound("cIN.doesNotContain=" + DEFAULT_C_IN);

        // Get all the personneList where cIN does not contain UPDATED_C_IN
        defaultPersonneShouldBeFound("cIN.doesNotContain=" + UPDATED_C_IN);
    }

    @Test
    @Transactional
    void getAllPersonnesByDateNaissanceIsEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where dateNaissance equals to DEFAULT_DATE_NAISSANCE
        defaultPersonneShouldBeFound("dateNaissance.equals=" + DEFAULT_DATE_NAISSANCE);

        // Get all the personneList where dateNaissance equals to UPDATED_DATE_NAISSANCE
        defaultPersonneShouldNotBeFound("dateNaissance.equals=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllPersonnesByDateNaissanceIsInShouldWork() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where dateNaissance in DEFAULT_DATE_NAISSANCE or UPDATED_DATE_NAISSANCE
        defaultPersonneShouldBeFound("dateNaissance.in=" + DEFAULT_DATE_NAISSANCE + "," + UPDATED_DATE_NAISSANCE);

        // Get all the personneList where dateNaissance equals to UPDATED_DATE_NAISSANCE
        defaultPersonneShouldNotBeFound("dateNaissance.in=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllPersonnesByDateNaissanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where dateNaissance is not null
        defaultPersonneShouldBeFound("dateNaissance.specified=true");

        // Get all the personneList where dateNaissance is null
        defaultPersonneShouldNotBeFound("dateNaissance.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonnesByDateNaissanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where dateNaissance is greater than or equal to DEFAULT_DATE_NAISSANCE
        defaultPersonneShouldBeFound("dateNaissance.greaterThanOrEqual=" + DEFAULT_DATE_NAISSANCE);

        // Get all the personneList where dateNaissance is greater than or equal to UPDATED_DATE_NAISSANCE
        defaultPersonneShouldNotBeFound("dateNaissance.greaterThanOrEqual=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllPersonnesByDateNaissanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where dateNaissance is less than or equal to DEFAULT_DATE_NAISSANCE
        defaultPersonneShouldBeFound("dateNaissance.lessThanOrEqual=" + DEFAULT_DATE_NAISSANCE);

        // Get all the personneList where dateNaissance is less than or equal to SMALLER_DATE_NAISSANCE
        defaultPersonneShouldNotBeFound("dateNaissance.lessThanOrEqual=" + SMALLER_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllPersonnesByDateNaissanceIsLessThanSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where dateNaissance is less than DEFAULT_DATE_NAISSANCE
        defaultPersonneShouldNotBeFound("dateNaissance.lessThan=" + DEFAULT_DATE_NAISSANCE);

        // Get all the personneList where dateNaissance is less than UPDATED_DATE_NAISSANCE
        defaultPersonneShouldBeFound("dateNaissance.lessThan=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllPersonnesByDateNaissanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where dateNaissance is greater than DEFAULT_DATE_NAISSANCE
        defaultPersonneShouldNotBeFound("dateNaissance.greaterThan=" + DEFAULT_DATE_NAISSANCE);

        // Get all the personneList where dateNaissance is greater than SMALLER_DATE_NAISSANCE
        defaultPersonneShouldBeFound("dateNaissance.greaterThan=" + SMALLER_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllPersonnesByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where phone equals to DEFAULT_PHONE
        defaultPersonneShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the personneList where phone equals to UPDATED_PHONE
        defaultPersonneShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllPersonnesByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultPersonneShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the personneList where phone equals to UPDATED_PHONE
        defaultPersonneShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllPersonnesByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where phone is not null
        defaultPersonneShouldBeFound("phone.specified=true");

        // Get all the personneList where phone is null
        defaultPersonneShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonnesByPhoneContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where phone contains DEFAULT_PHONE
        defaultPersonneShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the personneList where phone contains UPDATED_PHONE
        defaultPersonneShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllPersonnesByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where phone does not contain DEFAULT_PHONE
        defaultPersonneShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the personneList where phone does not contain UPDATED_PHONE
        defaultPersonneShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllPersonnesByRibIsEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where rib equals to DEFAULT_RIB
        defaultPersonneShouldBeFound("rib.equals=" + DEFAULT_RIB);

        // Get all the personneList where rib equals to UPDATED_RIB
        defaultPersonneShouldNotBeFound("rib.equals=" + UPDATED_RIB);
    }

    @Test
    @Transactional
    void getAllPersonnesByRibIsInShouldWork() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where rib in DEFAULT_RIB or UPDATED_RIB
        defaultPersonneShouldBeFound("rib.in=" + DEFAULT_RIB + "," + UPDATED_RIB);

        // Get all the personneList where rib equals to UPDATED_RIB
        defaultPersonneShouldNotBeFound("rib.in=" + UPDATED_RIB);
    }

    @Test
    @Transactional
    void getAllPersonnesByRibIsNullOrNotNull() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where rib is not null
        defaultPersonneShouldBeFound("rib.specified=true");

        // Get all the personneList where rib is null
        defaultPersonneShouldNotBeFound("rib.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonnesByRibContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where rib contains DEFAULT_RIB
        defaultPersonneShouldBeFound("rib.contains=" + DEFAULT_RIB);

        // Get all the personneList where rib contains UPDATED_RIB
        defaultPersonneShouldNotBeFound("rib.contains=" + UPDATED_RIB);
    }

    @Test
    @Transactional
    void getAllPersonnesByRibNotContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where rib does not contain DEFAULT_RIB
        defaultPersonneShouldNotBeFound("rib.doesNotContain=" + DEFAULT_RIB);

        // Get all the personneList where rib does not contain UPDATED_RIB
        defaultPersonneShouldBeFound("rib.doesNotContain=" + UPDATED_RIB);
    }

    @Test
    @Transactional
    void getAllPersonnesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where email equals to DEFAULT_EMAIL
        defaultPersonneShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the personneList where email equals to UPDATED_EMAIL
        defaultPersonneShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPersonnesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultPersonneShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the personneList where email equals to UPDATED_EMAIL
        defaultPersonneShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPersonnesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where email is not null
        defaultPersonneShouldBeFound("email.specified=true");

        // Get all the personneList where email is null
        defaultPersonneShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonnesByEmailContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where email contains DEFAULT_EMAIL
        defaultPersonneShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the personneList where email contains UPDATED_EMAIL
        defaultPersonneShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPersonnesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where email does not contain DEFAULT_EMAIL
        defaultPersonneShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the personneList where email does not contain UPDATED_EMAIL
        defaultPersonneShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPersonnesBySexIsEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where sex equals to DEFAULT_SEX
        defaultPersonneShouldBeFound("sex.equals=" + DEFAULT_SEX);

        // Get all the personneList where sex equals to UPDATED_SEX
        defaultPersonneShouldNotBeFound("sex.equals=" + UPDATED_SEX);
    }

    @Test
    @Transactional
    void getAllPersonnesBySexIsInShouldWork() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where sex in DEFAULT_SEX or UPDATED_SEX
        defaultPersonneShouldBeFound("sex.in=" + DEFAULT_SEX + "," + UPDATED_SEX);

        // Get all the personneList where sex equals to UPDATED_SEX
        defaultPersonneShouldNotBeFound("sex.in=" + UPDATED_SEX);
    }

    @Test
    @Transactional
    void getAllPersonnesBySexIsNullOrNotNull() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where sex is not null
        defaultPersonneShouldBeFound("sex.specified=true");

        // Get all the personneList where sex is null
        defaultPersonneShouldNotBeFound("sex.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonnesBySexContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where sex contains DEFAULT_SEX
        defaultPersonneShouldBeFound("sex.contains=" + DEFAULT_SEX);

        // Get all the personneList where sex contains UPDATED_SEX
        defaultPersonneShouldNotBeFound("sex.contains=" + UPDATED_SEX);
    }

    @Test
    @Transactional
    void getAllPersonnesBySexNotContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where sex does not contain DEFAULT_SEX
        defaultPersonneShouldNotBeFound("sex.doesNotContain=" + DEFAULT_SEX);

        // Get all the personneList where sex does not contain UPDATED_SEX
        defaultPersonneShouldBeFound("sex.doesNotContain=" + UPDATED_SEX);
    }

    @Test
    @Transactional
    void getAllPersonnesByReclamationIsEqualToSomething() throws Exception {
        Reclamation reclamation;
        if (TestUtil.findAll(em, Reclamation.class).isEmpty()) {
            personneRepository.saveAndFlush(personne);
            reclamation = ReclamationResourceIT.createEntity(em);
        } else {
            reclamation = TestUtil.findAll(em, Reclamation.class).get(0);
        }
        em.persist(reclamation);
        em.flush();
        personne.addReclamation(reclamation);
        personneRepository.saveAndFlush(personne);
        Long reclamationId = reclamation.getId();
        // Get all the personneList where reclamation equals to reclamationId
        defaultPersonneShouldBeFound("reclamationId.equals=" + reclamationId);

        // Get all the personneList where reclamation equals to (reclamationId + 1)
        defaultPersonneShouldNotBeFound("reclamationId.equals=" + (reclamationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPersonneShouldBeFound(String filter) throws Exception {
        restPersonneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personne.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].cIN").value(hasItem(DEFAULT_C_IN)))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].rib").value(hasItem(DEFAULT_RIB)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX)));

        // Check, that the count call also returns 1
        restPersonneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPersonneShouldNotBeFound(String filter) throws Exception {
        restPersonneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPersonneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPersonne() throws Exception {
        // Get the personne
        restPersonneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPersonne() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        int databaseSizeBeforeUpdate = personneRepository.findAll().size();

        // Update the personne
        Personne updatedPersonne = personneRepository.findById(personne.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPersonne are not directly saved in db
        em.detach(updatedPersonne);
        updatedPersonne
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .cIN(UPDATED_C_IN)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .phone(UPDATED_PHONE)
            .rib(UPDATED_RIB)
            .email(UPDATED_EMAIL)
            .sex(UPDATED_SEX);
        PersonneDTO personneDTO = personneMapper.toDto(updatedPersonne);

        restPersonneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personneDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personneDTO))
            )
            .andExpect(status().isOk());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
        Personne testPersonne = personneList.get(personneList.size() - 1);
        assertThat(testPersonne.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testPersonne.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testPersonne.getcIN()).isEqualTo(UPDATED_C_IN);
        assertThat(testPersonne.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testPersonne.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testPersonne.getRib()).isEqualTo(UPDATED_RIB);
        assertThat(testPersonne.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPersonne.getSex()).isEqualTo(UPDATED_SEX);
    }

    @Test
    @Transactional
    void putNonExistingPersonne() throws Exception {
        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
        personne.setId(longCount.incrementAndGet());

        // Create the Personne
        PersonneDTO personneDTO = personneMapper.toDto(personne);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personneDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonne() throws Exception {
        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
        personne.setId(longCount.incrementAndGet());

        // Create the Personne
        PersonneDTO personneDTO = personneMapper.toDto(personne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonne() throws Exception {
        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
        personne.setId(longCount.incrementAndGet());

        // Create the Personne
        PersonneDTO personneDTO = personneMapper.toDto(personne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personneDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonneWithPatch() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        int databaseSizeBeforeUpdate = personneRepository.findAll().size();

        // Update the personne using partial update
        Personne partialUpdatedPersonne = new Personne();
        partialUpdatedPersonne.setId(personne.getId());

        partialUpdatedPersonne.prenom(UPDATED_PRENOM).rib(UPDATED_RIB).email(UPDATED_EMAIL);

        restPersonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonne))
            )
            .andExpect(status().isOk());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
        Personne testPersonne = personneList.get(personneList.size() - 1);
        assertThat(testPersonne.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testPersonne.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testPersonne.getcIN()).isEqualTo(DEFAULT_C_IN);
        assertThat(testPersonne.getDateNaissance()).isEqualTo(DEFAULT_DATE_NAISSANCE);
        assertThat(testPersonne.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testPersonne.getRib()).isEqualTo(UPDATED_RIB);
        assertThat(testPersonne.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPersonne.getSex()).isEqualTo(DEFAULT_SEX);
    }

    @Test
    @Transactional
    void fullUpdatePersonneWithPatch() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        int databaseSizeBeforeUpdate = personneRepository.findAll().size();

        // Update the personne using partial update
        Personne partialUpdatedPersonne = new Personne();
        partialUpdatedPersonne.setId(personne.getId());

        partialUpdatedPersonne
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .cIN(UPDATED_C_IN)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .phone(UPDATED_PHONE)
            .rib(UPDATED_RIB)
            .email(UPDATED_EMAIL)
            .sex(UPDATED_SEX);

        restPersonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonne))
            )
            .andExpect(status().isOk());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
        Personne testPersonne = personneList.get(personneList.size() - 1);
        assertThat(testPersonne.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testPersonne.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testPersonne.getcIN()).isEqualTo(UPDATED_C_IN);
        assertThat(testPersonne.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testPersonne.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testPersonne.getRib()).isEqualTo(UPDATED_RIB);
        assertThat(testPersonne.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPersonne.getSex()).isEqualTo(UPDATED_SEX);
    }

    @Test
    @Transactional
    void patchNonExistingPersonne() throws Exception {
        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
        personne.setId(longCount.incrementAndGet());

        // Create the Personne
        PersonneDTO personneDTO = personneMapper.toDto(personne);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personneDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonne() throws Exception {
        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
        personne.setId(longCount.incrementAndGet());

        // Create the Personne
        PersonneDTO personneDTO = personneMapper.toDto(personne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonne() throws Exception {
        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
        personne.setId(longCount.incrementAndGet());

        // Create the Personne
        PersonneDTO personneDTO = personneMapper.toDto(personne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonneMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(personneDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonne() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        int databaseSizeBeforeDelete = personneRepository.findAll().size();

        // Delete the personne
        restPersonneMockMvc
            .perform(delete(ENTITY_API_URL_ID, personne.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

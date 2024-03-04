package com.reclamation.pfe.web.rest;

import com.reclamation.pfe.repository.PersonneRepository;
import com.reclamation.pfe.service.PersonneQueryService;
import com.reclamation.pfe.service.PersonneService;
import com.reclamation.pfe.service.criteria.PersonneCriteria;
import com.reclamation.pfe.service.dto.PersonneDTO;
import com.reclamation.pfe.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.reclamation.pfe.domain.Personne}.
 */
@RestController
@RequestMapping("/api/personnes")
public class PersonneResource {

    private final Logger log = LoggerFactory.getLogger(PersonneResource.class);

    private static final String ENTITY_NAME = "personne";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonneService personneService;

    private final PersonneRepository personneRepository;

    private final PersonneQueryService personneQueryService;

    public PersonneResource(
        PersonneService personneService,
        PersonneRepository personneRepository,
        PersonneQueryService personneQueryService
    ) {
        this.personneService = personneService;
        this.personneRepository = personneRepository;
        this.personneQueryService = personneQueryService;
    }

    /**
     * {@code POST  /personnes} : Create a new personne.
     *
     * @param personneDTO the personneDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personneDTO, or with status {@code 400 (Bad Request)} if the personne has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PersonneDTO> createPersonne(@RequestBody PersonneDTO personneDTO) throws URISyntaxException {
        log.debug("REST request to save Personne : {}", personneDTO);
        if (personneDTO.getId() != null) {
            throw new BadRequestAlertException("A new personne cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonneDTO result = personneService.save(personneDTO);
        return ResponseEntity
            .created(new URI("/api/personnes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /personnes/:id} : Updates an existing personne.
     *
     * @param id the id of the personneDTO to save.
     * @param personneDTO the personneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personneDTO,
     * or with status {@code 400 (Bad Request)} if the personneDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PersonneDTO> updatePersonne(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PersonneDTO personneDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Personne : {}, {}", id, personneDTO);
        if (personneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PersonneDTO result = personneService.update(personneDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personneDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /personnes/:id} : Partial updates given fields of an existing personne, field will ignore if it is null
     *
     * @param id the id of the personneDTO to save.
     * @param personneDTO the personneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personneDTO,
     * or with status {@code 400 (Bad Request)} if the personneDTO is not valid,
     * or with status {@code 404 (Not Found)} if the personneDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the personneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PersonneDTO> partialUpdatePersonne(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PersonneDTO personneDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Personne partially : {}, {}", id, personneDTO);
        if (personneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PersonneDTO> result = personneService.partialUpdate(personneDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personneDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /personnes} : get all the personnes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personnes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PersonneDTO>> getAllPersonnes(
        PersonneCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Personnes by criteria: {}", criteria);

        Page<PersonneDTO> page = personneQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /personnes/count} : count all the personnes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPersonnes(PersonneCriteria criteria) {
        log.debug("REST request to count Personnes by criteria: {}", criteria);
        return ResponseEntity.ok().body(personneQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /personnes/:id} : get the "id" personne.
     *
     * @param id the id of the personneDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personneDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PersonneDTO> getPersonne(@PathVariable("id") Long id) {
        log.debug("REST request to get Personne : {}", id);
        Optional<PersonneDTO> personneDTO = personneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personneDTO);
    }

    /**
     * {@code DELETE  /personnes/:id} : delete the "id" personne.
     *
     * @param id the id of the personneDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonne(@PathVariable("id") Long id) {
        log.debug("REST request to delete Personne : {}", id);
        personneService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

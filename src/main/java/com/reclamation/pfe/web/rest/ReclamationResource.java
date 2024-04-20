package com.reclamation.pfe.web.rest;

import com.reclamation.pfe.repository.ReclamationRepository;
import com.reclamation.pfe.service.ReclamationQueryService;
import com.reclamation.pfe.service.ReclamationService;
import com.reclamation.pfe.service.criteria.ReclamationCriteria;
import com.reclamation.pfe.service.dto.ReclamationDTO;
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
 * REST controller for managing {@link com.reclamation.pfe.domain.Reclamation}.
 */
@RestController
@RequestMapping("/api/reclamations")
public class ReclamationResource {

    private final Logger log = LoggerFactory.getLogger(ReclamationResource.class);

    private static final String ENTITY_NAME = "reclamation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReclamationService reclamationService;

    private final ReclamationRepository reclamationRepository;

    private final ReclamationQueryService reclamationQueryService;

    public ReclamationResource(
        ReclamationService reclamationService,
        ReclamationRepository reclamationRepository,
        ReclamationQueryService reclamationQueryService
    ) {
        this.reclamationService = reclamationService;
        this.reclamationRepository = reclamationRepository;
        this.reclamationQueryService = reclamationQueryService;
    }

    /**
     * {@code POST  /reclamations} : Create a new reclamation.
     *
     * @param reclamationDTO the reclamationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reclamationDTO, or with status {@code 400 (Bad Request)} if the reclamation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReclamationDTO> createReclamation(@RequestBody ReclamationDTO reclamationDTO) throws URISyntaxException {
        System.out.println("****" + reclamationDTO);
        log.debug("REST request to save Reclamation : {}", reclamationDTO);
        if (reclamationDTO.getId() != null) {
            throw new BadRequestAlertException("A new reclamation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReclamationDTO result = reclamationService.save(reclamationDTO);
        System.out.println("---" + result);
        return ResponseEntity
            .created(new URI("/api/reclamations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reclamations/:id} : Updates an existing reclamation.
     *
     * @param id the id of the reclamationDTO to save.
     * @param reclamationDTO the reclamationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reclamationDTO,
     * or with status {@code 400 (Bad Request)} if the reclamationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reclamationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReclamationDTO> updateReclamation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReclamationDTO reclamationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Reclamation : {}, {}", id, reclamationDTO);
        if (reclamationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reclamationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reclamationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReclamationDTO result = reclamationService.update(reclamationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reclamationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /reclamations/:id} : Partial updates given fields of an existing reclamation, field will ignore if it is null
     *
     * @param id the id of the reclamationDTO to save.
     * @param reclamationDTO the reclamationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reclamationDTO,
     * or with status {@code 400 (Bad Request)} if the reclamationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reclamationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reclamationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReclamationDTO> partialUpdateReclamation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReclamationDTO reclamationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Reclamation partially : {}, {}", id, reclamationDTO);
        if (reclamationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reclamationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reclamationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReclamationDTO> result = reclamationService.partialUpdate(reclamationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reclamationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /reclamations} : get all the reclamations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reclamations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReclamationDTO>> getAllReclamations(
        ReclamationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Reclamations by criteria: {}", criteria);

        Page<ReclamationDTO> page = reclamationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reclamations/count} : count all the reclamations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReclamations(ReclamationCriteria criteria) {
        log.debug("REST request to count Reclamations by criteria: {}", criteria);
        return ResponseEntity.ok().body(reclamationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /reclamations/:id} : get the "id" reclamation.
     *
     * @param id the id of the reclamationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reclamationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReclamationDTO> getReclamation(@PathVariable("id") Long id) {
        log.debug("REST request to get Reclamation : {}", id);
        Optional<ReclamationDTO> reclamationDTO = reclamationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reclamationDTO);
    }

    /**
     * {@code DELETE  /reclamations/:id} : delete the "id" reclamation.
     *
     * @param id the id of the reclamationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReclamation(@PathVariable("id") Long id) {
        log.debug("REST request to delete Reclamation : {}", id);
        reclamationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

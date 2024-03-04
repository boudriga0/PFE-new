package com.reclamation.pfe.web.rest;

import com.reclamation.pfe.repository.PieceJointeRepository;
import com.reclamation.pfe.service.PieceJointeQueryService;
import com.reclamation.pfe.service.PieceJointeService;
import com.reclamation.pfe.service.criteria.PieceJointeCriteria;
import com.reclamation.pfe.service.dto.PieceJointeDTO;
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
 * REST controller for managing {@link com.reclamation.pfe.domain.PieceJointe}.
 */
@RestController
@RequestMapping("/api/piece-jointes")
public class PieceJointeResource {

    private final Logger log = LoggerFactory.getLogger(PieceJointeResource.class);

    private static final String ENTITY_NAME = "pieceJointe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PieceJointeService pieceJointeService;

    private final PieceJointeRepository pieceJointeRepository;

    private final PieceJointeQueryService pieceJointeQueryService;

    public PieceJointeResource(
        PieceJointeService pieceJointeService,
        PieceJointeRepository pieceJointeRepository,
        PieceJointeQueryService pieceJointeQueryService
    ) {
        this.pieceJointeService = pieceJointeService;
        this.pieceJointeRepository = pieceJointeRepository;
        this.pieceJointeQueryService = pieceJointeQueryService;
    }

    /**
     * {@code POST  /piece-jointes} : Create a new pieceJointe.
     *
     * @param pieceJointeDTO the pieceJointeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pieceJointeDTO, or with status {@code 400 (Bad Request)} if the pieceJointe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PieceJointeDTO> createPieceJointe(@RequestBody PieceJointeDTO pieceJointeDTO) throws URISyntaxException {
        log.debug("REST request to save PieceJointe : {}", pieceJointeDTO);
        if (pieceJointeDTO.getId() != null) {
            throw new BadRequestAlertException("A new pieceJointe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PieceJointeDTO result = pieceJointeService.save(pieceJointeDTO);
        return ResponseEntity
            .created(new URI("/api/piece-jointes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /piece-jointes/:id} : Updates an existing pieceJointe.
     *
     * @param id the id of the pieceJointeDTO to save.
     * @param pieceJointeDTO the pieceJointeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pieceJointeDTO,
     * or with status {@code 400 (Bad Request)} if the pieceJointeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pieceJointeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PieceJointeDTO> updatePieceJointe(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PieceJointeDTO pieceJointeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PieceJointe : {}, {}", id, pieceJointeDTO);
        if (pieceJointeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pieceJointeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pieceJointeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PieceJointeDTO result = pieceJointeService.update(pieceJointeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pieceJointeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /piece-jointes/:id} : Partial updates given fields of an existing pieceJointe, field will ignore if it is null
     *
     * @param id the id of the pieceJointeDTO to save.
     * @param pieceJointeDTO the pieceJointeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pieceJointeDTO,
     * or with status {@code 400 (Bad Request)} if the pieceJointeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pieceJointeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pieceJointeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PieceJointeDTO> partialUpdatePieceJointe(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PieceJointeDTO pieceJointeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PieceJointe partially : {}, {}", id, pieceJointeDTO);
        if (pieceJointeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pieceJointeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pieceJointeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PieceJointeDTO> result = pieceJointeService.partialUpdate(pieceJointeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pieceJointeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /piece-jointes} : get all the pieceJointes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pieceJointes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PieceJointeDTO>> getAllPieceJointes(
        PieceJointeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PieceJointes by criteria: {}", criteria);

        Page<PieceJointeDTO> page = pieceJointeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /piece-jointes/count} : count all the pieceJointes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPieceJointes(PieceJointeCriteria criteria) {
        log.debug("REST request to count PieceJointes by criteria: {}", criteria);
        return ResponseEntity.ok().body(pieceJointeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /piece-jointes/:id} : get the "id" pieceJointe.
     *
     * @param id the id of the pieceJointeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pieceJointeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PieceJointeDTO> getPieceJointe(@PathVariable("id") Long id) {
        log.debug("REST request to get PieceJointe : {}", id);
        Optional<PieceJointeDTO> pieceJointeDTO = pieceJointeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pieceJointeDTO);
    }

    /**
     * {@code DELETE  /piece-jointes/:id} : delete the "id" pieceJointe.
     *
     * @param id the id of the pieceJointeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePieceJointe(@PathVariable("id") Long id) {
        log.debug("REST request to delete PieceJointe : {}", id);
        pieceJointeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

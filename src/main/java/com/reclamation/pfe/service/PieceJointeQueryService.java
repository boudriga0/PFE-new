package com.reclamation.pfe.service;

import com.reclamation.pfe.domain.*; // for static metamodels
import com.reclamation.pfe.domain.PieceJointe;
import com.reclamation.pfe.repository.PieceJointeRepository;
import com.reclamation.pfe.service.criteria.PieceJointeCriteria;
import com.reclamation.pfe.service.dto.PieceJointeDTO;
import com.reclamation.pfe.service.mapper.PieceJointeMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PieceJointe} entities in the database.
 * The main input is a {@link PieceJointeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PieceJointeDTO} or a {@link Page} of {@link PieceJointeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PieceJointeQueryService extends QueryService<PieceJointe> {

    private final Logger log = LoggerFactory.getLogger(PieceJointeQueryService.class);

    private final PieceJointeRepository pieceJointeRepository;

    private final PieceJointeMapper pieceJointeMapper;

    public PieceJointeQueryService(PieceJointeRepository pieceJointeRepository, PieceJointeMapper pieceJointeMapper) {
        this.pieceJointeRepository = pieceJointeRepository;
        this.pieceJointeMapper = pieceJointeMapper;
    }

    /**
     * Return a {@link List} of {@link PieceJointeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PieceJointeDTO> findByCriteria(PieceJointeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PieceJointe> specification = createSpecification(criteria);
        return pieceJointeMapper.toDto(pieceJointeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PieceJointeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PieceJointeDTO> findByCriteria(PieceJointeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PieceJointe> specification = createSpecification(criteria);
        return pieceJointeRepository.findAll(specification, page).map(pieceJointeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PieceJointeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PieceJointe> specification = createSpecification(criteria);
        return pieceJointeRepository.count(specification);
    }

    /**
     * Function to convert {@link PieceJointeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PieceJointe> createSpecification(PieceJointeCriteria criteria) {
        Specification<PieceJointe> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PieceJointe_.id));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), PieceJointe_.url));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), PieceJointe_.type));
            }
            if (criteria.getData() != null) {
                specification = specification.and(buildStringSpecification(criteria.getData(), PieceJointe_.data));
            }
            if (criteria.getReclamationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReclamationId(),
                            root -> root.join(PieceJointe_.reclamation, JoinType.LEFT).get(Reclamation_.id)
                        )
                    );
            }
        }
        return specification;
    }
}

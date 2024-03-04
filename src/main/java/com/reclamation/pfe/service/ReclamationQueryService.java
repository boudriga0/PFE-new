package com.reclamation.pfe.service;

import com.reclamation.pfe.domain.*; // for static metamodels
import com.reclamation.pfe.domain.Reclamation;
import com.reclamation.pfe.repository.ReclamationRepository;
import com.reclamation.pfe.service.criteria.ReclamationCriteria;
import com.reclamation.pfe.service.dto.ReclamationDTO;
import com.reclamation.pfe.service.mapper.ReclamationMapper;
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
 * Service for executing complex queries for {@link Reclamation} entities in the database.
 * The main input is a {@link ReclamationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReclamationDTO} or a {@link Page} of {@link ReclamationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReclamationQueryService extends QueryService<Reclamation> {

    private final Logger log = LoggerFactory.getLogger(ReclamationQueryService.class);

    private final ReclamationRepository reclamationRepository;

    private final ReclamationMapper reclamationMapper;

    public ReclamationQueryService(ReclamationRepository reclamationRepository, ReclamationMapper reclamationMapper) {
        this.reclamationRepository = reclamationRepository;
        this.reclamationMapper = reclamationMapper;
    }

    /**
     * Return a {@link List} of {@link ReclamationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReclamationDTO> findByCriteria(ReclamationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Reclamation> specification = createSpecification(criteria);
        return reclamationMapper.toDto(reclamationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ReclamationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReclamationDTO> findByCriteria(ReclamationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Reclamation> specification = createSpecification(criteria);
        return reclamationRepository.findAll(specification, page).map(reclamationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReclamationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Reclamation> specification = createSpecification(criteria);
        return reclamationRepository.count(specification);
    }

    /**
     * Function to convert {@link ReclamationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Reclamation> createSpecification(ReclamationCriteria criteria) {
        Specification<Reclamation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Reclamation_.id));
            }
            if (criteria.getCategorie() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCategorie(), Reclamation_.categorie));
            }
            if (criteria.getEtat() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEtat(), Reclamation_.etat));
            }
            if (criteria.getNumero() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumero(), Reclamation_.numero));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Reclamation_.date));
            }
            if (criteria.getPieceJointeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPieceJointeId(),
                            root -> root.join(Reclamation_.pieceJointes, JoinType.LEFT).get(PieceJointe_.id)
                        )
                    );
            }
            if (criteria.getCommentaireId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCommentaireId(),
                            root -> root.join(Reclamation_.commentaires, JoinType.LEFT).get(Commentaire_.id)
                        )
                    );
            }
            if (criteria.getPersonneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPersonneId(),
                            root -> root.join(Reclamation_.personne, JoinType.LEFT).get(Personne_.id)
                        )
                    );
            }
        }
        return specification;
    }
}

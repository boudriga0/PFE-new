package com.reclamation.pfe.service;

import com.reclamation.pfe.domain.*; // for static metamodels
import com.reclamation.pfe.domain.Commentaire;
import com.reclamation.pfe.repository.CommentaireRepository;
import com.reclamation.pfe.service.criteria.CommentaireCriteria;
import com.reclamation.pfe.service.dto.CommentaireDTO;
import com.reclamation.pfe.service.mapper.CommentaireMapper;
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
 * Service for executing complex queries for {@link Commentaire} entities in the database.
 * The main input is a {@link CommentaireCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CommentaireDTO} or a {@link Page} of {@link CommentaireDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CommentaireQueryService extends QueryService<Commentaire> {

    private final Logger log = LoggerFactory.getLogger(CommentaireQueryService.class);

    private final CommentaireRepository commentaireRepository;

    private final CommentaireMapper commentaireMapper;

    public CommentaireQueryService(CommentaireRepository commentaireRepository, CommentaireMapper commentaireMapper) {
        this.commentaireRepository = commentaireRepository;
        this.commentaireMapper = commentaireMapper;
    }

    /**
     * Return a {@link List} of {@link CommentaireDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CommentaireDTO> findByCriteria(CommentaireCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Commentaire> specification = createSpecification(criteria);
        return commentaireMapper.toDto(commentaireRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CommentaireDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CommentaireDTO> findByCriteria(CommentaireCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Commentaire> specification = createSpecification(criteria);
        return commentaireRepository.findAll(specification, page).map(commentaireMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommentaireCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Commentaire> specification = createSpecification(criteria);
        return commentaireRepository.count(specification);
    }

    /**
     * Function to convert {@link CommentaireCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Commentaire> createSpecification(CommentaireCriteria criteria) {
        Specification<Commentaire> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Commentaire_.id));
            }
            if (criteria.getContenu() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContenu(), Commentaire_.contenu));
            }
            if (criteria.getReclamationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReclamationId(),
                            root -> root.join(Commentaire_.reclamation, JoinType.LEFT).get(Reclamation_.id)
                        )
                    );
            }
        }
        return specification;
    }
}

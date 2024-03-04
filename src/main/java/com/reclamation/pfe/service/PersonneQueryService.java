package com.reclamation.pfe.service;

import com.reclamation.pfe.domain.*; // for static metamodels
import com.reclamation.pfe.domain.Personne;
import com.reclamation.pfe.repository.PersonneRepository;
import com.reclamation.pfe.service.criteria.PersonneCriteria;
import com.reclamation.pfe.service.dto.PersonneDTO;
import com.reclamation.pfe.service.mapper.PersonneMapper;
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
 * Service for executing complex queries for {@link Personne} entities in the database.
 * The main input is a {@link PersonneCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PersonneDTO} or a {@link Page} of {@link PersonneDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PersonneQueryService extends QueryService<Personne> {

    private final Logger log = LoggerFactory.getLogger(PersonneQueryService.class);

    private final PersonneRepository personneRepository;

    private final PersonneMapper personneMapper;

    public PersonneQueryService(PersonneRepository personneRepository, PersonneMapper personneMapper) {
        this.personneRepository = personneRepository;
        this.personneMapper = personneMapper;
    }

    /**
     * Return a {@link List} of {@link PersonneDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PersonneDTO> findByCriteria(PersonneCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Personne> specification = createSpecification(criteria);
        return personneMapper.toDto(personneRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PersonneDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonneDTO> findByCriteria(PersonneCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Personne> specification = createSpecification(criteria);
        return personneRepository.findAll(specification, page).map(personneMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PersonneCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Personne> specification = createSpecification(criteria);
        return personneRepository.count(specification);
    }

    /**
     * Function to convert {@link PersonneCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Personne> createSpecification(PersonneCriteria criteria) {
        Specification<Personne> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Personne_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Personne_.nom));
            }
            if (criteria.getPrenom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrenom(), Personne_.prenom));
            }
            if (criteria.getcIN() != null) {
                specification = specification.and(buildStringSpecification(criteria.getcIN(), Personne_.cIN));
            }
            if (criteria.getDateNaissance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateNaissance(), Personne_.dateNaissance));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Personne_.phone));
            }
            if (criteria.getRib() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRib(), Personne_.rib));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Personne_.email));
            }
            if (criteria.getSex() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSex(), Personne_.sex));
            }
            if (criteria.getReclamationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReclamationId(),
                            root -> root.join(Personne_.reclamations, JoinType.LEFT).get(Reclamation_.id)
                        )
                    );
            }
        }
        return specification;
    }
}

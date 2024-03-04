package com.reclamation.pfe.repository;

import com.reclamation.pfe.domain.Personne;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Personne entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonneRepository extends JpaRepository<Personne, Long>, JpaSpecificationExecutor<Personne> {}

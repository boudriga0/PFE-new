package com.reclamation.pfe.repository;

import com.reclamation.pfe.domain.PieceJointe;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PieceJointe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PieceJointeRepository extends JpaRepository<PieceJointe, Long>, JpaSpecificationExecutor<PieceJointe> {}

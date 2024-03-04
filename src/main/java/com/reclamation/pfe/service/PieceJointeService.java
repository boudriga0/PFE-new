package com.reclamation.pfe.service;

import com.reclamation.pfe.service.dto.PieceJointeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.reclamation.pfe.domain.PieceJointe}.
 */
public interface PieceJointeService {
    /**
     * Save a pieceJointe.
     *
     * @param pieceJointeDTO the entity to save.
     * @return the persisted entity.
     */
    PieceJointeDTO save(PieceJointeDTO pieceJointeDTO);

    /**
     * Updates a pieceJointe.
     *
     * @param pieceJointeDTO the entity to update.
     * @return the persisted entity.
     */
    PieceJointeDTO update(PieceJointeDTO pieceJointeDTO);

    /**
     * Partially updates a pieceJointe.
     *
     * @param pieceJointeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PieceJointeDTO> partialUpdate(PieceJointeDTO pieceJointeDTO);

    /**
     * Get all the pieceJointes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PieceJointeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" pieceJointe.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PieceJointeDTO> findOne(Long id);

    /**
     * Delete the "id" pieceJointe.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

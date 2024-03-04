package com.reclamation.pfe.service;

import com.reclamation.pfe.service.dto.CommentaireDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.reclamation.pfe.domain.Commentaire}.
 */
public interface CommentaireService {
    /**
     * Save a commentaire.
     *
     * @param commentaireDTO the entity to save.
     * @return the persisted entity.
     */
    CommentaireDTO save(CommentaireDTO commentaireDTO);

    /**
     * Updates a commentaire.
     *
     * @param commentaireDTO the entity to update.
     * @return the persisted entity.
     */
    CommentaireDTO update(CommentaireDTO commentaireDTO);

    /**
     * Partially updates a commentaire.
     *
     * @param commentaireDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CommentaireDTO> partialUpdate(CommentaireDTO commentaireDTO);

    /**
     * Get all the commentaires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CommentaireDTO> findAll(Pageable pageable);

    /**
     * Get the "id" commentaire.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CommentaireDTO> findOne(Long id);

    /**
     * Delete the "id" commentaire.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

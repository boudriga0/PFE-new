package com.reclamation.pfe.service.impl;

import com.reclamation.pfe.domain.Commentaire;
import com.reclamation.pfe.repository.CommentaireRepository;
import com.reclamation.pfe.service.CommentaireService;
import com.reclamation.pfe.service.dto.CommentaireDTO;
import com.reclamation.pfe.service.mapper.CommentaireMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.reclamation.pfe.domain.Commentaire}.
 */
@Service
@Transactional
public class CommentaireServiceImpl implements CommentaireService {

    private final Logger log = LoggerFactory.getLogger(CommentaireServiceImpl.class);

    private final CommentaireRepository commentaireRepository;

    private final CommentaireMapper commentaireMapper;

    public CommentaireServiceImpl(CommentaireRepository commentaireRepository, CommentaireMapper commentaireMapper) {
        this.commentaireRepository = commentaireRepository;
        this.commentaireMapper = commentaireMapper;
    }

    @Override
    public CommentaireDTO save(CommentaireDTO commentaireDTO) {
        log.debug("Request to save Commentaire : {}", commentaireDTO);
        Commentaire commentaire = commentaireMapper.toEntity(commentaireDTO);
        commentaire = commentaireRepository.save(commentaire);
        return commentaireMapper.toDto(commentaire);
    }

    @Override
    public CommentaireDTO update(CommentaireDTO commentaireDTO) {
        log.debug("Request to update Commentaire : {}", commentaireDTO);
        Commentaire commentaire = commentaireMapper.toEntity(commentaireDTO);
        commentaire = commentaireRepository.save(commentaire);
        return commentaireMapper.toDto(commentaire);
    }

    @Override
    public Optional<CommentaireDTO> partialUpdate(CommentaireDTO commentaireDTO) {
        log.debug("Request to partially update Commentaire : {}", commentaireDTO);

        return commentaireRepository
            .findById(commentaireDTO.getId())
            .map(existingCommentaire -> {
                commentaireMapper.partialUpdate(existingCommentaire, commentaireDTO);

                return existingCommentaire;
            })
            .map(commentaireRepository::save)
            .map(commentaireMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentaireDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Commentaires");
        return commentaireRepository.findAll(pageable).map(commentaireMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentaireDTO> findOne(Long id) {
        log.debug("Request to get Commentaire : {}", id);
        return commentaireRepository.findById(id).map(commentaireMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Commentaire : {}", id);
        commentaireRepository.deleteById(id);
    }
}

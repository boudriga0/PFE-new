package com.reclamation.pfe.service.impl;

import com.reclamation.pfe.domain.PieceJointe;
import com.reclamation.pfe.repository.PieceJointeRepository;
import com.reclamation.pfe.service.PieceJointeService;
import com.reclamation.pfe.service.dto.PieceJointeDTO;
import com.reclamation.pfe.service.mapper.PieceJointeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.reclamation.pfe.domain.PieceJointe}.
 */
@Service
@Transactional
public class PieceJointeServiceImpl implements PieceJointeService {

    private final Logger log = LoggerFactory.getLogger(PieceJointeServiceImpl.class);

    private final PieceJointeRepository pieceJointeRepository;

    private final PieceJointeMapper pieceJointeMapper;

    public PieceJointeServiceImpl(PieceJointeRepository pieceJointeRepository, PieceJointeMapper pieceJointeMapper) {
        this.pieceJointeRepository = pieceJointeRepository;
        this.pieceJointeMapper = pieceJointeMapper;
    }

    @Override
    public PieceJointeDTO save(PieceJointeDTO pieceJointeDTO) {
        log.debug("Request to save PieceJointe : {}", pieceJointeDTO);
        PieceJointe pieceJointe = pieceJointeMapper.toEntity(pieceJointeDTO);
        pieceJointe = pieceJointeRepository.save(pieceJointe);
        return pieceJointeMapper.toDto(pieceJointe);
    }

    @Override
    public PieceJointeDTO update(PieceJointeDTO pieceJointeDTO) {
        log.debug("Request to update PieceJointe : {}", pieceJointeDTO);
        PieceJointe pieceJointe = pieceJointeMapper.toEntity(pieceJointeDTO);
        pieceJointe = pieceJointeRepository.save(pieceJointe);
        return pieceJointeMapper.toDto(pieceJointe);
    }

    @Override
    public Optional<PieceJointeDTO> partialUpdate(PieceJointeDTO pieceJointeDTO) {
        log.debug("Request to partially update PieceJointe : {}", pieceJointeDTO);

        return pieceJointeRepository
            .findById(pieceJointeDTO.getId())
            .map(existingPieceJointe -> {
                pieceJointeMapper.partialUpdate(existingPieceJointe, pieceJointeDTO);

                return existingPieceJointe;
            })
            .map(pieceJointeRepository::save)
            .map(pieceJointeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PieceJointeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PieceJointes");
        return pieceJointeRepository.findAll(pageable).map(pieceJointeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PieceJointeDTO> findOne(Long id) {
        log.debug("Request to get PieceJointe : {}", id);
        return pieceJointeRepository.findById(id).map(pieceJointeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PieceJointe : {}", id);
        pieceJointeRepository.deleteById(id);
    }
}

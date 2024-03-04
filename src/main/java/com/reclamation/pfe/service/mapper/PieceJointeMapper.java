package com.reclamation.pfe.service.mapper;

import com.reclamation.pfe.domain.PieceJointe;
import com.reclamation.pfe.domain.Reclamation;
import com.reclamation.pfe.service.dto.PieceJointeDTO;
import com.reclamation.pfe.service.dto.ReclamationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PieceJointe} and its DTO {@link PieceJointeDTO}.
 */
@Mapper(componentModel = "spring")
public interface PieceJointeMapper extends EntityMapper<PieceJointeDTO, PieceJointe> {
    @Mapping(target = "reclamation", source = "reclamation", qualifiedByName = "reclamationId")
    PieceJointeDTO toDto(PieceJointe s);

    @Named("reclamationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReclamationDTO toDtoReclamationId(Reclamation reclamation);
}

package com.reclamation.pfe.service.mapper;

import com.reclamation.pfe.domain.Commentaire;
import com.reclamation.pfe.domain.Reclamation;
import com.reclamation.pfe.service.dto.CommentaireDTO;
import com.reclamation.pfe.service.dto.ReclamationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Commentaire} and its DTO {@link CommentaireDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommentaireMapper extends EntityMapper<CommentaireDTO, Commentaire> {
    @Mapping(target = "reclamation", source = "reclamation", qualifiedByName = "reclamationId")
    CommentaireDTO toDto(Commentaire s);

    @Named("reclamationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReclamationDTO toDtoReclamationId(Reclamation reclamation);
}

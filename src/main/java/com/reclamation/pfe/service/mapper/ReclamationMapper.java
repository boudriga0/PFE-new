package com.reclamation.pfe.service.mapper;

import com.reclamation.pfe.domain.Personne;
import com.reclamation.pfe.domain.Reclamation;
import com.reclamation.pfe.service.dto.PersonneDTO;
import com.reclamation.pfe.service.dto.ReclamationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reclamation} and its DTO {@link ReclamationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReclamationMapper extends EntityMapper<ReclamationDTO, Reclamation> {
    @Mapping(target = "personne", source = "personne", qualifiedByName = "personneId")
    ReclamationDTO toDto(Reclamation s);

    @Named("personneId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PersonneDTO toDtoPersonneId(Personne personne);
}

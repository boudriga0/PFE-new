package com.reclamation.pfe.service.mapper;

import com.reclamation.pfe.domain.Personne;
import com.reclamation.pfe.service.dto.PersonneDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Personne} and its DTO {@link PersonneDTO}.
 */
@Mapper(componentModel = "spring")
public interface PersonneMapper extends EntityMapper<PersonneDTO, Personne> {}

package com.mazimao.sportclub.service.mapper;

import com.mazimao.sportclub.domain.*;
import com.mazimao.sportclub.service.dto.ClubTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClubType} and its DTO {@link ClubTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClubTypeMapper extends EntityMapper<ClubTypeDTO, ClubType> {
    default ClubType fromId(Long id) {
        if (id == null) {
            return null;
        }
        ClubType clubType = new ClubType();
        clubType.setId(id);
        return clubType;
    }
}

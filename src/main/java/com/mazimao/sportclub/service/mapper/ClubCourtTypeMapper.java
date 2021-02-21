package com.mazimao.sportclub.service.mapper;

import com.mazimao.sportclub.domain.*;
import com.mazimao.sportclub.service.dto.ClubCourtTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClubCourtType} and its DTO {@link ClubCourtTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClubCourtTypeMapper extends EntityMapper<ClubCourtTypeDTO, ClubCourtType> {
    default ClubCourtType fromId(Long id) {
        if (id == null) {
            return null;
        }
        ClubCourtType clubCourtType = new ClubCourtType();
        clubCourtType.setId(id);
        return clubCourtType;
    }
}

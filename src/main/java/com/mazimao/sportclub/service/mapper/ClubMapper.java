package com.mazimao.sportclub.service.mapper;

import com.mazimao.sportclub.domain.*;
import com.mazimao.sportclub.service.dto.ClubDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Club} and its DTO {@link ClubDTO}.
 */
@Mapper(componentModel = "spring", uses = { ClubTypeMapper.class, OrganizationMapper.class })
public interface ClubMapper extends EntityMapper<ClubDTO, Club> {
    @Mapping(source = "clubType.id", target = "clubTypeId")
    @Mapping(source = "clubType.clubTypeName", target = "clubTypeClubTypeName")
    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "organization.organizationName", target = "organizationOrganizationName")
    ClubDTO toDto(Club club);

    @Mapping(source = "clubTypeId", target = "clubType")
    @Mapping(target = "clubCourts", ignore = true)
    @Mapping(target = "removeClubCourts", ignore = true)
    @Mapping(source = "organizationId", target = "organization")
    @Mapping(target = "clubManagers", ignore = true)
    @Mapping(target = "removeClubManager", ignore = true)
    Club toEntity(ClubDTO clubDTO);

    default Club fromId(Long id) {
        if (id == null) {
            return null;
        }
        Club club = new Club();
        club.setId(id);
        return club;
    }
}

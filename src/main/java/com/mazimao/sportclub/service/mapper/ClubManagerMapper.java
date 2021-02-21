package com.mazimao.sportclub.service.mapper;

import com.mazimao.sportclub.domain.*;
import com.mazimao.sportclub.service.dto.ClubManagerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClubManager} and its DTO {@link ClubManagerDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, ClubMapper.class, OrganizationMapper.class })
public interface ClubManagerMapper extends EntityMapper<ClubManagerDTO, ClubManager> {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "organization.organizationName", target = "organizationOrganizationName")
    ClubManagerDTO toDto(ClubManager clubManager);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "removeClubs", ignore = true)
    @Mapping(source = "organizationId", target = "organization")
    ClubManager toEntity(ClubManagerDTO clubManagerDTO);

    default ClubManager fromId(Long id) {
        if (id == null) {
            return null;
        }
        ClubManager clubManager = new ClubManager();
        clubManager.setId(id);
        return clubManager;
    }
}

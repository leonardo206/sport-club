package com.mazimao.sportclub.service.mapper;

import com.mazimao.sportclub.domain.*;
import com.mazimao.sportclub.service.dto.ClubCourtDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClubCourt} and its DTO {@link ClubCourtDTO}.
 */
@Mapper(componentModel = "spring", uses = { ClubCourtTypeMapper.class, ClubMapper.class })
public interface ClubCourtMapper extends EntityMapper<ClubCourtDTO, ClubCourt> {
    @Mapping(source = "clubCourtType.id", target = "clubCourtTypeId")
    @Mapping(source = "clubCourtType.clubCourtTypeName", target = "clubCourtTypeClubCourtTypeName")
    @Mapping(source = "club.id", target = "clubId")
    @Mapping(source = "club.clubName", target = "clubClubName")
    ClubCourtDTO toDto(ClubCourt clubCourt);

    @Mapping(source = "clubCourtTypeId", target = "clubCourtType")
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "removeBookings", ignore = true)
    @Mapping(source = "clubId", target = "club")
    ClubCourt toEntity(ClubCourtDTO clubCourtDTO);

    default ClubCourt fromId(Long id) {
        if (id == null) {
            return null;
        }
        ClubCourt clubCourt = new ClubCourt();
        clubCourt.setId(id);
        return clubCourt;
    }
}

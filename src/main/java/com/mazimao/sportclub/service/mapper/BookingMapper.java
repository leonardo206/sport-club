package com.mazimao.sportclub.service.mapper;

import com.mazimao.sportclub.domain.*;
import com.mazimao.sportclub.service.dto.BookingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Booking} and its DTO {@link BookingDTO}.
 */
@Mapper(componentModel = "spring", uses = { ClientMapper.class, ClubCourtMapper.class })
public interface BookingMapper extends EntityMapper<BookingDTO, Booking> {
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "clubCourt.id", target = "clubCourtId")
    @Mapping(source = "clubCourt.courtName", target = "clubCourtCourtName")
    BookingDTO toDto(Booking booking);

    @Mapping(source = "clientId", target = "client")
    @Mapping(source = "clubCourtId", target = "clubCourt")
    Booking toEntity(BookingDTO bookingDTO);

    default Booking fromId(Long id) {
        if (id == null) {
            return null;
        }
        Booking booking = new Booking();
        booking.setId(id);
        return booking;
    }
}

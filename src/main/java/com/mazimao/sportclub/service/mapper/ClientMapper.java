package com.mazimao.sportclub.service.mapper;

import com.mazimao.sportclub.domain.*;
import com.mazimao.sportclub.service.dto.ClientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Client} and its DTO {@link ClientDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    ClientDTO toDto(Client client);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "removeBookings", ignore = true)
    Client toEntity(ClientDTO clientDTO);

    default Client fromId(Long id) {
        if (id == null) {
            return null;
        }
        Client client = new Client();
        client.setId(id);
        return client;
    }
}

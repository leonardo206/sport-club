package com.mazimao.sportclub.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClientMapperTest {
    private ClientMapper clientMapper;

    @BeforeEach
    public void setUp() {
        clientMapper = new ClientMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(clientMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(clientMapper.fromId(null)).isNull();
    }
}

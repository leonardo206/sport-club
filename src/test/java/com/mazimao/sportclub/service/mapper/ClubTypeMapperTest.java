package com.mazimao.sportclub.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClubTypeMapperTest {
    private ClubTypeMapper clubTypeMapper;

    @BeforeEach
    public void setUp() {
        clubTypeMapper = new ClubTypeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(clubTypeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(clubTypeMapper.fromId(null)).isNull();
    }
}

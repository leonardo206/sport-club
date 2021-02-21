package com.mazimao.sportclub.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClubCourtTypeMapperTest {
    private ClubCourtTypeMapper clubCourtTypeMapper;

    @BeforeEach
    public void setUp() {
        clubCourtTypeMapper = new ClubCourtTypeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(clubCourtTypeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(clubCourtTypeMapper.fromId(null)).isNull();
    }
}

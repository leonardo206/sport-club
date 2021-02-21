package com.mazimao.sportclub.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClubCourtMapperTest {
    private ClubCourtMapper clubCourtMapper;

    @BeforeEach
    public void setUp() {
        clubCourtMapper = new ClubCourtMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(clubCourtMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(clubCourtMapper.fromId(null)).isNull();
    }
}

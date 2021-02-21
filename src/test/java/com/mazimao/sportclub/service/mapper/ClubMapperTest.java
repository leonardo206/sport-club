package com.mazimao.sportclub.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClubMapperTest {
    private ClubMapper clubMapper;

    @BeforeEach
    public void setUp() {
        clubMapper = new ClubMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(clubMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(clubMapper.fromId(null)).isNull();
    }
}

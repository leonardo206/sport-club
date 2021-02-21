package com.mazimao.sportclub.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClubManagerMapperTest {
    private ClubManagerMapper clubManagerMapper;

    @BeforeEach
    public void setUp() {
        clubManagerMapper = new ClubManagerMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(clubManagerMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(clubManagerMapper.fromId(null)).isNull();
    }
}

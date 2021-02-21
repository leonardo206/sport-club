package com.mazimao.sportclub.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserDetailsMapperTest {
    private UserDetailsMapper userDetailsMapper;

    @BeforeEach
    public void setUp() {
        userDetailsMapper = new UserDetailsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(userDetailsMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(userDetailsMapper.fromId(null)).isNull();
    }
}

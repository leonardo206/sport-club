package com.mazimao.sportclub.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrganizationMapperTest {
    private OrganizationMapper organizationMapper;

    @BeforeEach
    public void setUp() {
        organizationMapper = new OrganizationMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(organizationMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(organizationMapper.fromId(null)).isNull();
    }
}

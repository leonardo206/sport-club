package com.mazimao.sportclub.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mazimao.sportclub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class ClubTypeDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClubTypeDTO.class);
        ClubTypeDTO clubTypeDTO1 = new ClubTypeDTO();
        clubTypeDTO1.setId(1L);
        ClubTypeDTO clubTypeDTO2 = new ClubTypeDTO();
        assertThat(clubTypeDTO1).isNotEqualTo(clubTypeDTO2);
        clubTypeDTO2.setId(clubTypeDTO1.getId());
        assertThat(clubTypeDTO1).isEqualTo(clubTypeDTO2);
        clubTypeDTO2.setId(2L);
        assertThat(clubTypeDTO1).isNotEqualTo(clubTypeDTO2);
        clubTypeDTO1.setId(null);
        assertThat(clubTypeDTO1).isNotEqualTo(clubTypeDTO2);
    }
}

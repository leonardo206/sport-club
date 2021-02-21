package com.mazimao.sportclub.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mazimao.sportclub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class ClubManagerDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClubManagerDTO.class);
        ClubManagerDTO clubManagerDTO1 = new ClubManagerDTO();
        clubManagerDTO1.setId(1L);
        ClubManagerDTO clubManagerDTO2 = new ClubManagerDTO();
        assertThat(clubManagerDTO1).isNotEqualTo(clubManagerDTO2);
        clubManagerDTO2.setId(clubManagerDTO1.getId());
        assertThat(clubManagerDTO1).isEqualTo(clubManagerDTO2);
        clubManagerDTO2.setId(2L);
        assertThat(clubManagerDTO1).isNotEqualTo(clubManagerDTO2);
        clubManagerDTO1.setId(null);
        assertThat(clubManagerDTO1).isNotEqualTo(clubManagerDTO2);
    }
}

package com.mazimao.sportclub.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mazimao.sportclub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class ClubCourtTypeDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClubCourtTypeDTO.class);
        ClubCourtTypeDTO clubCourtTypeDTO1 = new ClubCourtTypeDTO();
        clubCourtTypeDTO1.setId(1L);
        ClubCourtTypeDTO clubCourtTypeDTO2 = new ClubCourtTypeDTO();
        assertThat(clubCourtTypeDTO1).isNotEqualTo(clubCourtTypeDTO2);
        clubCourtTypeDTO2.setId(clubCourtTypeDTO1.getId());
        assertThat(clubCourtTypeDTO1).isEqualTo(clubCourtTypeDTO2);
        clubCourtTypeDTO2.setId(2L);
        assertThat(clubCourtTypeDTO1).isNotEqualTo(clubCourtTypeDTO2);
        clubCourtTypeDTO1.setId(null);
        assertThat(clubCourtTypeDTO1).isNotEqualTo(clubCourtTypeDTO2);
    }
}

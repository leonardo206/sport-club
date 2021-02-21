package com.mazimao.sportclub.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mazimao.sportclub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class ClubCourtDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClubCourtDTO.class);
        ClubCourtDTO clubCourtDTO1 = new ClubCourtDTO();
        clubCourtDTO1.setId(1L);
        ClubCourtDTO clubCourtDTO2 = new ClubCourtDTO();
        assertThat(clubCourtDTO1).isNotEqualTo(clubCourtDTO2);
        clubCourtDTO2.setId(clubCourtDTO1.getId());
        assertThat(clubCourtDTO1).isEqualTo(clubCourtDTO2);
        clubCourtDTO2.setId(2L);
        assertThat(clubCourtDTO1).isNotEqualTo(clubCourtDTO2);
        clubCourtDTO1.setId(null);
        assertThat(clubCourtDTO1).isNotEqualTo(clubCourtDTO2);
    }
}

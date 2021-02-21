package com.mazimao.sportclub.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mazimao.sportclub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class ClubCourtTypeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClubCourtType.class);
        ClubCourtType clubCourtType1 = new ClubCourtType();
        clubCourtType1.setId(1L);
        ClubCourtType clubCourtType2 = new ClubCourtType();
        clubCourtType2.setId(clubCourtType1.getId());
        assertThat(clubCourtType1).isEqualTo(clubCourtType2);
        clubCourtType2.setId(2L);
        assertThat(clubCourtType1).isNotEqualTo(clubCourtType2);
        clubCourtType1.setId(null);
        assertThat(clubCourtType1).isNotEqualTo(clubCourtType2);
    }
}

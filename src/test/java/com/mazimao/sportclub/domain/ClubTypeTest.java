package com.mazimao.sportclub.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mazimao.sportclub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class ClubTypeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClubType.class);
        ClubType clubType1 = new ClubType();
        clubType1.setId(1L);
        ClubType clubType2 = new ClubType();
        clubType2.setId(clubType1.getId());
        assertThat(clubType1).isEqualTo(clubType2);
        clubType2.setId(2L);
        assertThat(clubType1).isNotEqualTo(clubType2);
        clubType1.setId(null);
        assertThat(clubType1).isNotEqualTo(clubType2);
    }
}

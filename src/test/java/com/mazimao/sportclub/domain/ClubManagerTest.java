package com.mazimao.sportclub.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mazimao.sportclub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class ClubManagerTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClubManager.class);
        ClubManager clubManager1 = new ClubManager();
        clubManager1.setId(1L);
        ClubManager clubManager2 = new ClubManager();
        clubManager2.setId(clubManager1.getId());
        assertThat(clubManager1).isEqualTo(clubManager2);
        clubManager2.setId(2L);
        assertThat(clubManager1).isNotEqualTo(clubManager2);
        clubManager1.setId(null);
        assertThat(clubManager1).isNotEqualTo(clubManager2);
    }
}

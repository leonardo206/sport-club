package com.mazimao.sportclub.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mazimao.sportclub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class ClubCourtTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClubCourt.class);
        ClubCourt clubCourt1 = new ClubCourt();
        clubCourt1.setId(1L);
        ClubCourt clubCourt2 = new ClubCourt();
        clubCourt2.setId(clubCourt1.getId());
        assertThat(clubCourt1).isEqualTo(clubCourt2);
        clubCourt2.setId(2L);
        assertThat(clubCourt1).isNotEqualTo(clubCourt2);
        clubCourt1.setId(null);
        assertThat(clubCourt1).isNotEqualTo(clubCourt2);
    }
}

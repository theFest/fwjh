package com.fwjh.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.fwjh.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TopicsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Topics.class);
        Topics topics1 = new Topics();
        topics1.setId(1L);
        Topics topics2 = new Topics();
        topics2.setId(topics1.getId());
        assertThat(topics1).isEqualTo(topics2);
        topics2.setId(2L);
        assertThat(topics1).isNotEqualTo(topics2);
        topics1.setId(null);
        assertThat(topics1).isNotEqualTo(topics2);
    }
}

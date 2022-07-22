package com.fwjh.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.fwjh.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SourcesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sources.class);
        Sources sources1 = new Sources();
        sources1.setId(1L);
        Sources sources2 = new Sources();
        sources2.setId(sources1.getId());
        assertThat(sources1).isEqualTo(sources2);
        sources2.setId(2L);
        assertThat(sources1).isNotEqualTo(sources2);
        sources1.setId(null);
        assertThat(sources1).isNotEqualTo(sources2);
    }
}

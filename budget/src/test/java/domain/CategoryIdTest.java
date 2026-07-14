package domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public final class CategoryIdTest {
    @Test
    void 카테고리_ID는_문자열_값을_가진다() {
        Identifier<String> id = new CategoryId("category-1");

        assertThat(id.value()).isEqualTo("category-1");
    }
}

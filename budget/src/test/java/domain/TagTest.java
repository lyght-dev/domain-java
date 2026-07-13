package domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public final class TagTest {
    @Test
    void 태그는_10자까지_가질_수_있다() {
        assertThat(new Tag("가".repeat(10)).value()).hasSize(10);
    }

    @Test
    void 태그는_11자부터_가질_수_없다() {
        assertThatThrownBy(() -> new Tag("가".repeat(11)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"\t", "\n", "\r", "\0", "\33", "\177"})
    void 태그는_출력할_수_없는_문자를_제거한다(String nonPrintable) {
        assertThat(new Tag("태그" + nonPrintable + "이름").value()).isEqualTo("태그이름");
    }
}

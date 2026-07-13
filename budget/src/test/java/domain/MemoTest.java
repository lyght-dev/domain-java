package domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public final class MemoTest {
    @Test
    void 메모는_100자까지_가질_수_있다() {
        assertThat(new Memo("가".repeat(100)).value()).hasSize(100);
    }

    @Test
    void 메모는_101자부터_가질_수_없다() {
        assertThatThrownBy(() -> new Memo("가".repeat(101)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"\t", "\n", "\r", "\0", "\33", "\177"})
    void 메모는_출력할_수_없는_문자를_제거한다(String nonPrintable) {
        assertThat(new Memo("메모" + nonPrintable + "내용").value()).isEqualTo("메모내용");
    }
}

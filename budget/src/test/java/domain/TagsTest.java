package domain;

import java.util.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public final class TagsTest {
    @Test
    void 태그는_5개까지_가질_수_있다() {
        var tags = new Tags(List.of(
                new Tag("1"), new Tag("2"), new Tag("3"), new Tag("4"), new Tag("5")
        ));

        assertThat(tags.values()).hasSize(5);
    }

    @Test
    void 태그는_6개부터_가질_수_없다() {
        var values = List.of(
                new Tag("1"), new Tag("2"), new Tag("3"), new Tag("4"), new Tag("5"), new Tag("6")
        );

        assertThatThrownBy(() -> new Tags(values)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 태그를_마지막에_추가할_수_있다() {
        var first = new Tag("1");
        var second = new Tag("2");

        assertThat(new Tags(List.of(first)).append(second).values())
                .containsExactly(first, second);
    }

    @Test
    void 특정_태그를_제거할_수_있다() {
        var first = new Tag("1");
        var second = new Tag("2");

        assertThat(new Tags(List.of(first, second)).remove(first).values())
                .containsExactly(second);
    }

    @Test
    void 모든_태그를_제거할_수_있다() {
        var tags = new Tags(List.of(new Tag("1")));

        assertThat(tags.clear().values()).isEmpty();
    }
}

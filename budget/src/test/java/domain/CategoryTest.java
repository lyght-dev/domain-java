package domain;

import java.time.Instant;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public final class CategoryTest {
    @Test
    void 카테고리는_ID와_이름과_생성_시각을_가진다() {
        var id = new CategoryId("category-1");
        var name = new CategoryName("식비");
        var beforeCreation = Instant.now();

        var category = new Category(id, name);
        var afterCreation = Instant.now();

        assertThat(category.id()).isEqualTo(id);
        assertThat(category.name()).isEqualTo(name);
        assertThat(category.createdAt()).isBetween(beforeCreation, afterCreation);
    }

    @Test
    void 카테고리_이름은_10자를_초과할_수_없다() {
        assertThatThrownBy(() -> new CategoryName("가나다라마바사아자차카"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

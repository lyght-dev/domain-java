package domain;

import java.time.Instant;
import java.util.Objects;

public final class Category {
    private final CategoryId id;
    private final CategoryName name;
    private final Instant createdAt;

    public Category(CategoryId id, CategoryName name) {
        this.id = Objects.requireNonNull(id, "카테고리 ID는 null일 수 없습니다.");
        this.name = Objects.requireNonNull(name, "카테고리 이름은 null일 수 없습니다.");
        this.createdAt = Instant.now();
    }

    public CategoryId id() {
        return id;
    }

    public CategoryName name() {
        return name;
    }

    public Instant createdAt() {
        return createdAt;
    }
}

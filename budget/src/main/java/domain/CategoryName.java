package domain;

import java.util.Objects;

public record CategoryName(String value) {
    public CategoryName {
        value = Objects.requireNonNull(value, "카테고리 이름은 null일 수 없습니다.");
        if (value.codePointCount(0, value.length()) > 10) {
            throw new IllegalArgumentException("카테고리 이름은 10자를 초과할 수 없습니다.");
        }
    }
}

// 카테고리 네임은 최대 10자.

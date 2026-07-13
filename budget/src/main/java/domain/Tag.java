package domain;

import java.util.Objects;

public record Tag(String value) {
    public Tag {
        value = Objects.requireNonNull(value, "태그는 null일 수 없습니다.");
        if (value.codePointCount(0, value.length()) > 10) {
            throw new IllegalArgumentException("태그는 10자를 초과할 수 없습니다.");
        }
    }

    private static String printable(String value) {
        return value.codePoints()
                .filter(codePoint -> !Character.isISOControl(codePoint))
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}

package domain;

import java.util.Objects;

public record Memo(String value) {
    public static Memo empty() {
        return new Memo("");
    }

    public Memo {
        value = Objects.requireNonNull(value, "메모는 null일 수 없습니다.");
        value = printable(value);
        if (value.codePointCount(0, value.length()) > 100) {
            throw new IllegalArgumentException("메모는 100자를 초과할 수 없습니다.");
        }
    }

    private static String printable(String value) {
        return value.codePoints()
                .filter(codePoint -> !Character.isISOControl(codePoint))
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}

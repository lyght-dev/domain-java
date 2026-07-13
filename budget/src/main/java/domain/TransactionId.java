package domain;

public record TransactionId(String value) implements Identifier<String> {
    private static final long MAX_SEQUENCE = 999_999_999_999L;

    public TransactionId {
        if (!value.matches("Tx-\\d{3}-\\d{3}-\\d{3}-\\d{3}")) {
            throw new IllegalArgumentException("거래 ID 형식이 올바르지 않습니다.");
        }
    }

    public TransactionId next() {
        var sequence = Long.parseLong(value.substring(3, 6) + value.substring(7, 10)
                + value.substring(11, 14) + value.substring(15, 18));
        if (sequence == MAX_SEQUENCE) {
            throw new IllegalStateException("다음 거래 ID를 만들 수 없습니다.");
        }
        var next = "%012d".formatted(sequence + 1);
        return new TransactionId("Tx-%s-%s-%s-%s".formatted(
                next.substring(0, 3), next.substring(3, 6), next.substring(6, 9), next.substring(9, 12)
        ));
    }
}

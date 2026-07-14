package domain;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public final class Transaction {
    private final TransactionId id;
    private final TransactionType type;
    private final CategoryId categoryId;
    private final Won amount;
    private final Memo memo;
    private final Tags tags;
    private final Instant occurredAt;

    public Transaction(
            TransactionId id,
            TransactionType type,
            CategoryId categoryId,
            Won amount,
            Memo memo,
            Tags tags,
            Instant occurredAt
    ) {
        this.id = id;
        this.type = type;
        this.categoryId = categoryId;
        this.amount = amount;
        this.memo = memo;
        this.tags = tags;
        this.occurredAt = occurredAt;
    }

    public static Transaction of(
            String id,
            String type,
            String categoryId,
            long amount,
            String memo,
            List<String> tags,
            Instant occurredAt
    ) {
        var memoValue = Objects.requireNonNullElse(memo, "");
        var tagValues = Objects.requireNonNullElse(tags, List.of());
        return new Transaction(
                new TransactionId(id),
                TransactionType.valueOf(type),
                new CategoryId(categoryId),
                new Won(amount),
                memoValue.isEmpty() ? Memo.empty() : new Memo(memoValue),
                tagValues.isEmpty() ? Tags.empty() : Tags.of(tagValues.toArray(String[]::new)),
                Objects.requireNonNull(occurredAt, "거래 시각은 null일 수 없습니다.")
        );
    }

    public TransactionId id() {
        return id;
    }

    public TransactionType type() {
        return type;
    }

    public CategoryId categoryId() {
        return categoryId;
    }

    public Won amount() {
        return amount;
    }

    public Memo memo() {
        return memo;
    }

    public Tags tags() {
        return tags;
    }

    public Instant occurredAt() {
        return occurredAt;
    }
}

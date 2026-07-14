package domain;

import java.time.Instant;

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

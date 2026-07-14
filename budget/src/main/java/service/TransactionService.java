package service;

import domain.Transaction;
import domain.TransactionId;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import repository.TransactionRepository;

public final class TransactionService {
    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public Transaction addTransaction(
            String id, String type, String categoryId, long amount, String memo, Instant occurredAt, List<String> tags
    ) {
        return repository.save(Transaction.of(id, type, categoryId, amount, memo, tags, occurredAt));
    }

    public Transaction findTransaction(String id) {
        return repository.findById(new TransactionId(id))
                .orElseThrow(() -> new IllegalArgumentException("거래를 찾을 수 없습니다."));
    }

    public List<Transaction> findTransactions() {
        return repository.findAll();
    }

    public List<Transaction> findTransactions(int limit) {
        return repository.findAll(limit);
    }

    public Transaction patchTransaction(
            String id, String type, String categoryId, Long amount, String memo, Instant occurredAt, List<String> tags
    ) {
        var current = findTransaction(id);
        var updated = Transaction.of(
                id,
                Objects.requireNonNullElseGet(type, () -> current.type().name()),
                Objects.requireNonNullElseGet(categoryId, () -> current.categoryId().value()),
                Objects.requireNonNullElseGet(amount, () -> current.amount().amount()),
                Objects.requireNonNullElseGet(memo, () -> current.memo().value()),
                Objects.requireNonNullElseGet(tags, current.tags()::texts),
                Objects.requireNonNullElseGet(occurredAt, current::occurredAt)
        );
        return repository.save(updated);
    }

    public void deleteTransaction(String id) {
        repository.deleteById(new TransactionId(id));
    }
}

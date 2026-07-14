package repository;

import domain.Transaction;
import domain.TransactionId;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    Transaction save(Transaction transaction);

    Optional<Transaction> findById(TransactionId id);

    List<Transaction> findAll();

    List<Transaction> findAll(int limit);

    void deleteById(TransactionId id);
}

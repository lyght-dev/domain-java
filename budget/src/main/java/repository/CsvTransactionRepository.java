package repository;

import domain.Transaction;
import domain.TransactionId;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class CsvTransactionRepository implements TransactionRepository {
    private static final String HEADER = "id,type,categoryId,amount,memo,tags,occurredAt";

    private final Path path;

    public CsvTransactionRepository() {
        this(Path.of("data", "transactions.csv"));
    }

    public CsvTransactionRepository(Path path) {
        this.path = path;
    }

    @Override
    public Transaction save(Transaction transaction) {
        var transactions = new ArrayList<>(findAll());
        for (var index = 0; index < transactions.size(); index++) {
            if (transactions.get(index).id().equals(transaction.id())) {
                transactions.set(index, transaction);
                write(transactions);
                return transaction;
            }
        }
        transactions.add(transaction);
        write(transactions);
        return transaction;
    }

    @Override
    public Optional<Transaction> findById(TransactionId id) {
        return findAll().stream().filter(transaction -> transaction.id().equals(id)).findFirst();
    }

    @Override
    public List<Transaction> findAll() {
        if (!Files.exists(path)) {
            return List.of();
        }
        try {
            return Files.readAllLines(path, StandardCharsets.UTF_8).stream()
                    .skip(1)
                    .filter(line -> !line.isEmpty())
                    .map(CsvTransactionRepository::transaction)
                    .toList();
        } catch (IOException exception) {
            throw new UncheckedIOException("거래 CSV를 읽을 수 없습니다.", exception);
        }
    }

    @Override
    public List<Transaction> findAll(int limit) {
        if (limit < 0) {
            return List.of();
        }
        return findAll().stream().limit(limit).toList();
    }

    @Override
    public void deleteById(TransactionId id) {
        var transactions = new ArrayList<>(findAll());
        if (!transactions.removeIf(transaction -> transaction.id().equals(id))) {
            throw new IllegalArgumentException("거래를 찾을 수 없습니다.");
        }
        write(transactions);
    }

    private void write(List<Transaction> transactions) {
        var lines = new ArrayList<String>();
        lines.add(HEADER);
        transactions.stream().map(CsvTransactionRepository::line).forEach(lines::add);

        try {
            var parent = path.toAbsolutePath().getParent();
            Files.createDirectories(parent);
            var temporary = parent.resolve(path.getFileName() + ".tmp");
            Files.write(temporary, lines, StandardCharsets.UTF_8);
            try {
                Files.move(temporary, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException ignored) {
                Files.move(temporary, path, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException exception) {
            throw new UncheckedIOException("거래 CSV를 저장할 수 없습니다.", exception);
        }
    }

    private static String line(Transaction transaction) {
        return CsvCodec.writeRow(List.of(
                transaction.id().value(),
                transaction.type().name(),
                transaction.categoryId().value(),
                Long.toString(transaction.amount().amount()),
                transaction.memo().value(),
                CsvCodec.writeRow(transaction.tags().values().stream().map(tag -> tag.value()).toList()),
                transaction.occurredAt().toString()
        ));
    }

    private static Transaction transaction(String line) {
        var values = CsvCodec.readRow(line);
        if (values.size() != 7) {
            throw new IllegalArgumentException("거래 CSV 형식이 올바르지 않습니다.");
        }
        var tags = CsvCodec.readRow(values.get(5));
        return Transaction.of(
                values.get(0), values.get(1), values.get(2), Long.parseLong(values.get(3)),
                values.get(4), tags, Instant.parse(values.get(6))
        );
    }

}

package repository;

import domain.CategoryId;
import domain.Memo;
import domain.Tags;
import domain.Transaction;
import domain.TransactionId;
import domain.TransactionType;
import domain.Won;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public final class CsvTransactionRepositoryTest {
    @TempDir
    Path directory;

    private Path path;
    private CsvTransactionRepository repository;

    @BeforeEach
    void setUp() {
        path = directory.resolve("transactions.csv");
        repository = new CsvTransactionRepository(path);
    }

    @Test
    void 거래를_CSV에_저장하고_다시_조회한다() {
        var transaction = transaction("001", 10_000);

        repository.save(transaction);

        var found = new CsvTransactionRepository(path).findById(transaction.id());
        assertThat(found).hasValueSatisfying(value -> {
            assertThat(value.id()).isEqualTo(transaction.id());
            assertThat(value.memo()).isEqualTo(new Memo("메모"));
            assertThat(value.tags()).isEqualTo(Tags.of("1", "2"));
        });
    }

    @Test
    void 같은_ID로_저장하면_기존_거래를_대체한다() {
        repository.save(transaction("001", 10_000));

        repository.save(transaction("001", 20_000));

        assertThat(repository.findAll()).hasSize(1);
        assertThat(repository.findById(new TransactionId("Tx-000-000-000-001")))
                .hasValueSatisfying(value -> assertThat(value.amount()).isEqualTo(new Won(20_000)));
    }

    @Test
    void 구분자가_포함된_태그를_보존한다() {
        var transaction = Transaction.of(
                "Tx-000-000-000-001", "EXPENSE", "category-1", 10_000,
                "메모", List.of("태그|이름", "경로\\이름"), Instant.parse("2000-05-19T00:00:00Z")
        );

        repository.save(transaction);

        assertThat(new CsvTransactionRepository(path).findById(transaction.id()))
                .hasValueSatisfying(value -> assertThat(value.tags()).isEqualTo(Tags.of("태그|이름", "경로\\이름")));
    }

    @Test
    void limit만큼_거래를_조회한다() {
        repository.save(transaction("001", 10_000));
        repository.save(transaction("002", 20_000));

        assertThat(repository.findAll(1)).extracting(value -> value.id().value())
                .containsExactly("Tx-000-000-000-001");
        assertThat(repository.findAll(-1)).isEmpty();
    }

    @Test
    void ID로_거래를_삭제한다() {
        var id = new TransactionId("Tx-000-000-000-001");
        repository.save(transaction("001", 10_000));

        repository.deleteById(id);

        assertThat(repository.findById(id)).isEmpty();
    }

    @Test
    void 없는_ID를_삭제하면_예외가_발생한다() {
        assertThatThrownBy(() -> repository.deleteById(new TransactionId("Tx-000-000-000-001")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Transaction transaction(String sequence, long amount) {
        return new Transaction(
                new TransactionId("Tx-000-000-000-" + sequence),
                TransactionType.EXPENSE,
                new CategoryId("category-1"),
                new Won(amount),
                new Memo("메모"),
                Tags.of("1", "2"),
                Instant.parse("2000-05-19T00:00:00Z")
        );
    }
}

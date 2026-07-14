package service;

import domain.Transaction;
import domain.TransactionId;
import domain.TransactionType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.TransactionRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public final class TransactionServiceTest {
    private FakeTransactionRepository repository;
    private TransactionService service;

    @BeforeEach
    void setUp() {
        repository = new FakeTransactionRepository();
        service = new TransactionService(repository);
    }

    @Test
    void 기본값으로_거래를_추가하고_저장한다() {
        // when
        var transaction = service.addTransaction(
                "Tx-000-000-000-001", "EXPENSE", "category-1", 10_000,
                "메모", Instant.parse("2000-05-19T00:00:00Z"), List.of("1", "2")
        );

        // then
        assertThat(repository.findById(transaction.id())).contains(transaction);
        assertThat(transaction.type()).isEqualTo(TransactionType.EXPENSE);
        assertThat(transaction.amount().amount()).isEqualTo(10_000);
        assertThat(transaction.memo().value()).isEqualTo("메모");
        assertThat(transaction.tags().values()).extracting(tag -> tag.value()).containsExactly("1", "2");
    }

    @Test
    void 빈_메모와_태그로_거래를_추가한다() {
        // when
        var transaction = service.addTransaction(
                "Tx-000-000-000-001", "EXPENSE", "category-1", 10_000,
                "", Instant.parse("2000-05-19T00:00:00Z"), List.of()
        );

        // then
        assertThat(transaction.memo().value()).isEmpty();
        assertThat(transaction.tags().values()).isEmpty();
    }

    @Test
    void ID로_거래를_조회한다() {
        // given
        var expected = transaction("001");
        repository.save(expected);

        // when
        var actual = service.findTransaction("Tx-000-000-000-001");

        // then
        assertThat(actual).isSameAs(expected);
    }

    @Test
    void 없는_ID의_거래를_조회하면_예외가_발생한다() {
        // when
        // then
        assertThatThrownBy(() -> service.findTransaction("Tx-000-000-000-001"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 거래를_최대_limit개_조회한다() {
        // given
        var first = transaction("001");
        var second = transaction("002");
        var third = transaction("003");
        repository.save(first);
        repository.save(second);
        repository.save(third);

        // when
        var results = service.findTransactions(2);

        // then
        assertThat(results).containsExactly(first, second);
    }

    @Test
    void 모든_거래를_조회한다() {
        // given
        var first = transaction("001");
        var second = transaction("002");
        repository.save(first);
        repository.save(second);

        // when
        var results = service.findTransactions();

        // then
        assertThat(results).containsExactly(first, second);
    }

    @Test
    void 음수_limit이면_빈_목록을_반환한다() {
        // given
        repository.save(transaction("001"));

        // when
        var results = service.findTransactions(-1);

        // then
        assertThat(results).isEmpty();
    }

    @Test
    void null이_아닌_값만_변경한다() {
        // given
        repository.save(transaction("001"));

        // when
        var updated = service.patchTransaction(
                "Tx-000-000-000-001", null, null, 20_000L, null, null, null
        );

        // then
        assertThat(updated.id().value()).isEqualTo("Tx-000-000-000-001");
        assertThat(updated.amount().amount()).isEqualTo(20_000);
        assertThat(updated.memo().value()).isEqualTo("메모");
        assertThat(repository.findById(updated.id())).contains(updated);
    }

    @Test
    void ID로_거래를_삭제한다() {
        // given
        repository.save(transaction("001"));

        // when
        service.deleteTransaction("Tx-000-000-000-001");

        // then
        assertThat(repository.findById(new TransactionId("Tx-000-000-000-001"))).isEmpty();
    }

    @Test
    void 없는_ID의_거래를_삭제하면_예외가_발생한다() {
        // when
        // then
        assertThatThrownBy(() -> service.deleteTransaction("Tx-000-000-000-001"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Transaction transaction(String sequence) {
        return new Transaction(
                new TransactionId("Tx-000-000-000-" + sequence),
                TransactionType.EXPENSE,
                new domain.CategoryId("category-1"),
                new domain.Won(10_000),
                new domain.Memo("메모"),
                domain.Tags.of("1"),
                Instant.parse("2000-05-19T00:00:00Z")
        );
    }

    private static final class FakeTransactionRepository implements TransactionRepository {
        private final List<Transaction> transactions = new ArrayList<>();

        @Override
        public Transaction save(Transaction transaction) {
            for (var index = 0; index < transactions.size(); index++) {
                if (transactions.get(index).id().equals(transaction.id())) {
                    transactions.set(index, transaction);
                    return transaction;
                }
            }
            transactions.add(transaction);
            return transaction;
        }

        @Override
        public Optional<Transaction> findById(TransactionId id) {
            return transactions.stream().filter(transaction -> transaction.id().equals(id)).findFirst();
        }

        @Override
        public List<Transaction> findAll(int limit) {
            if (limit < 0) {
                return List.of();
            }
            return List.copyOf(transactions.subList(0, Math.min(limit, transactions.size())));
        }

        @Override
        public List<Transaction> findAll() {
            return List.copyOf(transactions);
        }

        @Override
        public void deleteById(TransactionId id) {
            if (!transactions.removeIf(transaction -> transaction.id().equals(id))) {
                throw new IllegalArgumentException("거래를 찾을 수 없습니다.");
            }
        }
    }
}

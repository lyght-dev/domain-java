package domain;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public final class TransactionTest {
    @Test
    void 거래는_필수_정보를_가진다() {
        // given
        var id = new TransactionId("Tx-000-000-000-001");
        var categoryId = new CategoryId("category-1");
        var amount = new Won(10_000);
        var memo = new Memo("메모");
        var tags = Tags.of("태그1", "태그2");
        var occurredAt = Instant.parse("2000-05-19T00:00:00Z");

        // when
        var transaction = new Transaction(id, TransactionType.EXPENSE, categoryId, amount, memo, tags, occurredAt);

        // then
        assertThat(transaction.id()).isEqualTo(id);
        assertThat(transaction.type()).isEqualTo(TransactionType.EXPENSE);
        assertThat(transaction.categoryId()).isEqualTo(categoryId);
        assertThat(transaction.amount()).isEqualTo(amount);
        assertThat(transaction.memo()).isEqualTo(memo);
        assertThat(transaction.tags()).isEqualTo(tags);
        assertThat(transaction.occurredAt()).isEqualTo(occurredAt);
    }

    @Test
    void 거래는_원시_값으로_생성할_수_있다() {
        // given
        var transaction = Transaction.of(
                "Tx-000-000-000-001",
                "EXPENSE",
                "category-1",
                10_000,
                "메모",
                List.of("1", "2"),
                Instant.parse("2000-05-19T00:00:00Z")
        );

        // then
        assertThat(transaction.id()).isEqualTo(new TransactionId("Tx-000-000-000-001"));
        assertThat(transaction.memo()).isEqualTo(new Memo("메모"));
        assertThat(transaction.tags()).isEqualTo(Tags.of("1", "2"));
    }

    @Test
    void 거래는_메모와_태그_없이_생성할_수_있다() {
        // given
        var transaction = new Transaction(
                new TransactionId("Tx-000-000-000-001"),
                TransactionType.EXPENSE,
                new CategoryId("category-1"),
                new Won(10_000),
                Memo.empty(),
                Tags.empty(),
                Instant.parse("2000-05-19T00:00:00Z")
        );

        // then
        assertThat(transaction.memo()).isEqualTo(Memo.empty());
        assertThat(transaction.tags()).isEqualTo(Tags.empty());
    }
}

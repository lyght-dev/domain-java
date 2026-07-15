package domain;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public final class MonthlyTransactionSummaryTest {
    @Test
    void 월별_거래로_수입과_지출과_잔액과_지출_상위_카테고리를_집계한다() {
        var transactions = List.of(
                transaction("001", TransactionType.INCOME, "category-0", 1_000, "2000-05-19T00:00:00Z"),
                transaction("002", TransactionType.EXPENSE, "category-1", 300, "2000-05-19T01:00:00Z"),
                transaction("003", TransactionType.EXPENSE, "category-1", 200, "2000-05-19T02:00:00Z"),
                transaction("004", TransactionType.EXPENSE, "category-2", 400, "2000-05-19T03:00:00Z")
        );

        var summary = MonthlyTransactionSummary.from(transactions);

        assertThat(summary.hasData()).isTrue();
        assertThat(summary.totalIncome()).isEqualTo(new Won(1_000));
        assertThat(summary.totalExpense()).isEqualTo(new Won(900));
        assertThat(summary.balance()).isEqualTo(new Won(100));
        assertThat(summary.balanceStatus()).isEqualTo(BalanceStatus.SURPLUS);
        assertThat(summary.top(1))
                .containsExactly(new CategoryExpense(new CategoryId("category-1"), new Won(500)));
        assertThat(summary.top(2)).containsExactly(
                new CategoryExpense(new CategoryId("category-1"), new Won(500)),
                new CategoryExpense(new CategoryId("category-2"), new Won(400))
        );
    }

    @Test
    void 거래가_없는_달은_데이터가_없다() {
        var summary = MonthlyTransactionSummary.from(List.of());

        assertThat(summary.hasData()).isFalse();
        assertThat(summary.balance()).isEqualTo(new Won(0));
        assertThat(summary.balanceStatus()).isEqualTo(BalanceStatus.BALANCED);
    }

    @Test
    void 지출이_수입보다_크면_적자이다() {
        var summary = MonthlyTransactionSummary.from(List.of(
                transaction("001", TransactionType.INCOME, "category-0", 100, "2000-05-19T00:00:00Z"),
                transaction("002", TransactionType.EXPENSE, "category-1", 300, "2000-05-19T01:00:00Z")
        ));

        assertThat(summary.balance()).isEqualTo(new Won(200));
        assertThat(summary.balanceStatus()).isEqualTo(BalanceStatus.DEFICIT);
    }

    private static Transaction transaction(
            String sequence, TransactionType type, String categoryId, long amount, String occurredAt
    ) {
        return new Transaction(
                new TransactionId("Tx-000-000-000-%s".formatted(sequence)),
                type,
                new CategoryId(categoryId),
                new Won(amount),
                Memo.empty(),
                Tags.empty(),
                Instant.parse(occurredAt)
        );
    }
}

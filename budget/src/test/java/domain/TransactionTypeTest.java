package domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public final class TransactionTypeTest {
    @Test
    void 거래_유형은_수입과_지출이다() {
        assertThat(TransactionType.values()).containsExactly(TransactionType.INCOME, TransactionType.EXPENSE);
    }
}

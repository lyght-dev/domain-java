package domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public final class TransactionIdTest {
    @Test
    void 거래_ID는_문자열_값을_가진다() {
        Identifier<String> id = new TransactionId("Tx-000-000-000-000");

        assertThat(id.value()).isEqualTo("Tx-000-000-000-000");
    }

    @Test
    void 다음_거래_ID는_1씩_증가한다() {
        var id = new TransactionId("Tx-000-000-000-000");

        assertThat(id.next().value()).isEqualTo("Tx-000-000-000-001");
    }

    @Test
    void 거래_ID는_자릿수_올림을_처리한다() {
        var id = new TransactionId("Tx-000-000-000-999");

        assertThat(id.next().value()).isEqualTo("Tx-000-000-001-000");
    }

    @Test
    void 거래_ID는_정해진_형식이_아니면_만들_수_없다() {
        assertThatThrownBy(() -> new TransactionId("transaction-1"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

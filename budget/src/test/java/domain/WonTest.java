package domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public final class WonTest {
    @Test
    void 원화끼리_사칙연산을_할_수_있다() {
        var ten = new Won(10);
        var two = new Won(2);

        assertThat(ten.plus(two).amount()).isEqualTo(12);
        assertThat(ten.minus(two).amount()).isEqualTo(8);
        assertThat(ten.times(two).amount()).isEqualTo(20);
        assertThat(ten.dividedBy(two).amount()).isEqualTo(5);
    }

    @Test
    void 원화끼리_동등과_대소를_비교할_수_있다() {
        var ten = new Won(10);

        assertThat(ten).isEqualTo(new Won(10));
        assertThat(ten).isGreaterThan(new Won(9));
        assertThat(ten).isLessThan(new Won(11));
    }

    @Test
    void 음수_원화는_만들_수_없다() {
        assertThatThrownBy(() -> new Won(-1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Won(1).minus(new Won(2))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 원화_연산의_오버플로는_허용하지_않는다() {
        assertThatThrownBy(() -> new Won(Long.MAX_VALUE).plus(new Won(1)))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Won(Long.MAX_VALUE).times(new Won(2)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

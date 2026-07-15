package domain;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public final class BudgetTest {
    @Test
    void 예산은_설정된_달과_금액을_가진다() {
        var budgetMonth = new BudgetMonth(Year.of(2000), Month.MAY);
        var amount = new Won(500_000);

        var budget = new Budget(budgetMonth, amount);

        assertThat(budget.budgetMonth()).isEqualTo(budgetMonth);
        assertThat(budget.amount()).isEqualTo(amount);
        assertThat(budget.appliesTo(LocalDate.of(2000, 5, 1))).isTrue();
        assertThat(budget.appliesTo(LocalDate.of(2000, 5, 31))).isTrue();
        assertThat(budget.appliesTo(LocalDate.of(2000, 6, 1))).isFalse();
    }

    @Test
    void 예산은_금액이_차지하는_비율을_반환한다() {
        var budget = new Budget(new BudgetMonth(Year.of(2000), Month.MAY), new Won(500_000));

        assertThat(budget.percentageOf(new Won(125_000))).isEqualTo(25.0);
    }

    @Test
    void 예산_사용률은_소수점_둘째_자리까지_반환한다() {
        var budget = new Budget(new BudgetMonth(Year.of(2000), Month.MAY), new Won(3));

        assertThat(budget.percentageOf(new Won(1))).isEqualTo(33.33);
    }

    @Test
    void 예산이_0원이면_비율을_계산할_수_없다() {
        var budget = new Budget(new BudgetMonth(Year.of(2000), Month.MAY), new Won(0));

        assertThatThrownBy(() -> budget.percentageOf(new Won(1)))
                .isInstanceOf(IllegalStateException.class);
    }
}

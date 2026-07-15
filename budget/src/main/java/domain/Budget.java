package domain;

import java.time.LocalDate;
import java.util.Objects;

public final class Budget {
    private final BudgetMonth budgetMonth;
    private final Won amount;

    public Budget(BudgetMonth budgetMonth, Won amount) {
        this.budgetMonth = Objects.requireNonNull(budgetMonth, "예산 적용 월은 null일 수 없습니다.");
        this.amount = Objects.requireNonNull(amount, "예산 금액은 null일 수 없습니다.");
    }

    public BudgetMonth budgetMonth() {
        return budgetMonth;
    }

    public Won amount() {
        return amount;
    }

    public boolean appliesTo(LocalDate date) {
        return budgetMonth.includes(date);
    }

    public double percentageOf(Won amount) {
        Objects.requireNonNull(amount, "비율을 계산할 금액은 null일 수 없습니다.");
        if (this.amount.amount() == 0) {
            throw new IllegalStateException("0원 예산의 비율은 계산할 수 없습니다.");
        }
        return Math.round(amount.amount() * 10_000.0 / this.amount.amount()) / 100.0;
    }
}

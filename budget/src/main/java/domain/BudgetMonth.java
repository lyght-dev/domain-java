package domain;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Objects;

public record BudgetMonth(Year year, Month month) {
    public BudgetMonth {
        year = Objects.requireNonNull(year, "예산 연도는 null일 수 없습니다.");
        month = Objects.requireNonNull(month, "예산 월은 null일 수 없습니다.");
    }

    public boolean includes(LocalDate date) {
        return year.equals(Year.from(date)) && month == date.getMonth();
    }
}

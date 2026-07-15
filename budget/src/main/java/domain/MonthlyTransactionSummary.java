package domain;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class MonthlyTransactionSummary {
    private final Won totalIncome;
    private final Won totalExpense;
    private final Won balance;
    private final BalanceStatus balanceStatus;
    private final Map<CategoryId, Won> expensesByCategory;
    private final boolean hasData;

    private MonthlyTransactionSummary(
            Won totalIncome,
            Won totalExpense,
            Won balance,
            BalanceStatus balanceStatus,
            Map<CategoryId, Won> expensesByCategory,
            boolean hasData
    ) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.balance = balance;
        this.balanceStatus = balanceStatus;
        this.expensesByCategory = Map.copyOf(expensesByCategory);
        this.hasData = hasData;
    }

    public static MonthlyTransactionSummary from(List<Transaction> transactions) {
        Objects.requireNonNull(transactions, "월별 거래는 null일 수 없습니다.");
        var totalIncome = totalOf(transactions, TransactionType.INCOME);
        var totalExpense = totalOf(transactions, TransactionType.EXPENSE);
        var expensesByCategory = transactions.stream()
                .filter(transaction -> transaction.type() == TransactionType.EXPENSE)
                .collect(Collectors.toMap(Transaction::categoryId, Transaction::amount, Won::plus));
        var balanceStatus = balanceStatus(totalIncome, totalExpense);
        var balance = balanceStatus == BalanceStatus.DEFICIT
                ? totalExpense.minus(totalIncome)
                : totalIncome.minus(totalExpense);
        return new MonthlyTransactionSummary(
                totalIncome,
                totalExpense,
                balance,
                balanceStatus,
                expensesByCategory,
                !transactions.isEmpty()
        );
    }

    public Won totalIncome() {
        return totalIncome;
    }

    public Won totalExpense() {
        return totalExpense;
    }

    public Won balance() {
        return balance;
    }

    public BalanceStatus balanceStatus() {
        return balanceStatus;
    }

    public List<CategoryExpense> top(int top) {
        if (top < 0) {
            throw new IllegalArgumentException("상위 카테고리 개수는 음수일 수 없습니다.");
        }
        return expensesByCategory.entrySet().stream()
                .map(entry -> new CategoryExpense(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(CategoryExpense::amount).reversed()
                        .thenComparing(expense -> expense.categoryId().value()))
                .limit(top)
                .toList();
    }

    public boolean hasData() {
        return hasData;
    }

    private static Won totalOf(List<Transaction> transactions, TransactionType type) {
        return transactions.stream()
                .filter(transaction -> transaction.type() == type)
                .map(Transaction::amount)
                .reduce(new Won(0), Won::plus);
    }

    private static BalanceStatus balanceStatus(Won totalIncome, Won totalExpense) {
        var comparison = totalIncome.compareTo(totalExpense);
        if (comparison > 0) {
            return BalanceStatus.SURPLUS;
        }
        if (comparison < 0) {
            return BalanceStatus.DEFICIT;
        }
        return BalanceStatus.BALANCED;
    }
}

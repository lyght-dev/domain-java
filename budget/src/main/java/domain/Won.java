package domain;

public record Won(long amount) implements Comparable<Won> {
    public Won {
        if (amount < 0) {
            throw new IllegalArgumentException("원은 음수가 될 수 없습니다.");
        }
    }

    public Won plus(Won other) {
        try {
            return new Won(Math.addExact(amount, other.amount));
        } catch (ArithmeticException exception) {
            throw new IllegalArgumentException("원의 범위를 초과했습니다.", exception);
        }
    }

    public Won minus(Won other) {
        return new Won(amount - other.amount);
    }

    public Won times(Won other) {
        try {
            return new Won(Math.multiplyExact(amount, other.amount));
        } catch (ArithmeticException exception) {
            throw new IllegalArgumentException("원의 범위를 초과했습니다.", exception);
        }
    }

    public Won dividedBy(Won other) {
        return new Won(amount / other.amount);
    }

    @Override
    public int compareTo(Won other) {
        return Long.compare(amount, other.amount);
    }
}

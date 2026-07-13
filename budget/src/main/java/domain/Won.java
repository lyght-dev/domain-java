package domain;

public final class Won implements Comparable<Won> {
    public Won(long amount) {
    }

    public long amount() {
        return 0;
    }

    public Won plus(Won other) {
        return this;
    }

    public Won minus(Won other) {
        return this;
    }

    public Won times(Won other) {
        return this;
    }

    public Won dividedBy(Won other) {
        return this;
    }

    @Override
    public int compareTo(Won other) {
        return 0;
    }
}

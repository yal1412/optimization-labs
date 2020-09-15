package ru.sberbank.demo1;

import java.util.Objects;

public class Comparison<T> {
    T prev;
    T next;
    T diff; //on equality? zero or null

    public Comparison(T prev, T next, T diff) {
        this.prev = prev;
        this.next = next;
        this.diff = diff;
    }

    public static Comparison<Double> difference(Double prev, Double next) {
        return new Comparison<>(prev, next, update(prev, next));
    }

    public static Comparison<Object> update(Object prev, Object next) {
        return new Comparison<>(prev, next, next);
    }

    private static Double update(Double prev, Double next) {
        if (prev == null) {
            return next;
        } else {
            return next == null ? prev : Double.valueOf(next - prev);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comparison<?> that = (Comparison<?>) o;
        return Objects.equals(prev, that.prev) &&
                Objects.equals(next, that.next) &&
                Objects.equals(diff, that.diff);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prev, next, diff);
    }
}
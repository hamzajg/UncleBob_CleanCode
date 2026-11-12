package com.hamzajg.cleancode.money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * An amount in a single currency, stored as a whole number of cents.
 *
 * <p>Money is a value object: it is immutable, it validates itself, and every
 * operation returns a new instance. Keeping the amount as an integer avoids the
 * rounding drift of {@code double} arithmetic.
 */
public record Money(long cents) implements Comparable<Money> {

    /** The neutral element for {@link #plus(Money)}. */
    public static final Money ZERO = new Money(0);

    /** One hundred percent, in basis points. */
    private static final int WHOLE = 10_000;

    public Money {
        if (cents < 0) {
            throw new IllegalArgumentException("an amount cannot be negative: " + cents);
        }
    }

    /**
     * Parses a decimal amount such as {@code "19.99"} or {@code "7"}.
     *
     * @throws IllegalArgumentException when the text is not a non-negative amount
     */
    public static Money of(String amount) {
        Objects.requireNonNull(amount, "amount");
        String trimmed = amount.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("an amount cannot be blank");
        }

        try {
            BigDecimal value = new BigDecimal(trimmed);
            return new Money(value.movePointRight(2).setScale(0, RoundingMode.HALF_UP).longValueExact());
        } catch (NumberFormatException | ArithmeticException e) {
            throw new IllegalArgumentException("not an amount: " + amount, e);
        }
    }

    public Money plus(Money other) {
        Objects.requireNonNull(other, "other");
        return new Money(cents + other.cents);
    }

    public Money minus(Money other) {
        Objects.requireNonNull(other, "other");
        return new Money(cents - other.cents);
    }

    public Money times(int multiplier) {
        if (multiplier < 0) {
            throw new IllegalArgumentException("a multiplier cannot be negative: " + multiplier);
        }
        return new Money(cents * multiplier);
    }

    /**
     * Returns this share of the amount, in basis points (1 basis point is
     * 0.01%), rounded half up to the nearest cent.
     */
    public Money portion(int basisPoints) {
        if (basisPoints < 0 || basisPoints > WHOLE) {
            throw new IllegalArgumentException("basis points must be between 0 and 10000: " + basisPoints);
        }
        return new Money(Math.round(cents * (double) basisPoints / WHOLE));
    }

    public boolean isAtLeast(Money other) {
        return compareTo(other) >= 0;
    }

    @Override
    public int compareTo(Money other) {
        return Long.compare(cents, other.cents);
    }

    /** Formats the amount with exactly two decimals, e.g. {@code "19.99"}. */
    @Override
    public String toString() {
        return BigDecimal.valueOf(cents, 2).toPlainString();
    }
}

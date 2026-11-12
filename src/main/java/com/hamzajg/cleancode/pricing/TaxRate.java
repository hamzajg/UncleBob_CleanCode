package com.hamzajg.cleancode.pricing;

import com.hamzajg.cleancode.money.Money;

/**
 * A sales tax rate, in basis points so that rates such as 8.5% are exact.
 */
public record TaxRate(int basisPoints) {

    public TaxRate {
        if (basisPoints < 0 || basisPoints > 10_000) {
            throw new IllegalArgumentException("a tax rate must be between 0% and 100%: " + basisPoints);
        }
    }

    public static TaxRate ofPercent(int percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("a percentage must be between 0 and 100: " + percent);
        }
        return new TaxRate(percent * 100);
    }

    public Money taxOn(Money amount) {
        return amount.portion(basisPoints);
    }

    /** Formats the rate as a percentage, e.g. {@code "20%"} or {@code "8.5%"} */
    public String label() {
        if (basisPoints % 100 == 0) {
            return (basisPoints / 100) + "%";
        }
        return (basisPoints / 100.0) + "%";
    }
}

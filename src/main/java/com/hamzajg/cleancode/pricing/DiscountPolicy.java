package com.hamzajg.cleancode.pricing;

import com.hamzajg.cleancode.money.Money;

/**
 * Decides how much to take off a cart.
 *
 * <p>A policy is a single, named rule. Composing rules belongs to whoever
 * assembles the checkout, not to the policy itself.
 */
@FunctionalInterface
public interface DiscountPolicy {

    Money discountFor(Money subtotal);

    static DiscountPolicy none() {
        return subtotal -> Money.ZERO;
    }

    /**
     * Takes {@code percent} off the whole cart once the subtotal reaches
     * {@code threshold}.
     */
    static DiscountPolicy percentageAbove(Money threshold, int percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("a percentage must be between 0 and 100: " + percent);
        }
        return subtotal -> subtotal.isAtLeast(threshold) ? subtotal.portion(percent * 100) : Money.ZERO;
    }
}

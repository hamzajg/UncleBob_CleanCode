package com.hamzajg.cleancode.pricing;

import com.hamzajg.cleancode.money.Money;

import java.util.Objects;

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

    /**
     * Takes a fixed amount off the cart.
     *
     * <p>A coupon worth more than the cart never makes the discount exceed the
     * subtotal, so a checkout can never end up paying the customer.
     */
    static DiscountPolicy fixedOff(Money amount) {
        Objects.requireNonNull(amount, "amount");
        return subtotal -> amount.isAtLeast(subtotal) ? subtotal : amount;
    }
}

package com.hamzajg.cleancode.pricing;

import com.hamzajg.cleancode.money.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DiscountPolicyTest {

    @Test
    void noPolicyTakesNothingOff() {
        assertEquals(Money.ZERO, DiscountPolicy.none().discountFor(Money.of("100.00")));
    }

    @Test
    void takesNothingOffBelowTheThreshold() {
        DiscountPolicy policy = DiscountPolicy.percentageAbove(Money.of("50.00"), 10);

        assertEquals(Money.ZERO, policy.discountFor(Money.of("49.99")));
    }

    @Test
    void takesThePercentageOffAtTheThreshold() {
        DiscountPolicy policy = DiscountPolicy.percentageAbove(Money.of("50.00"), 10);

        assertEquals(Money.of("5.00"), policy.discountFor(Money.of("50.00")));
    }

    @Test
    void takesThePercentageOffAboveTheThreshold() {
        DiscountPolicy policy = DiscountPolicy.percentageAbove(Money.of("50.00"), 10);

        assertEquals(Money.of("10.49"), policy.discountFor(Money.of("104.90")));
    }

    @Test
    void roundsTheDiscountToTheNearestCent() {
        DiscountPolicy policy = DiscountPolicy.percentageAbove(Money.ZERO, 10);

        assertEquals(Money.of("2.00"), policy.discountFor(Money.of("19.99")));
    }

    @Test
    void takesAFixedAmountOff() {
        DiscountPolicy policy = DiscountPolicy.fixedOff(Money.of("5.00"));

        assertEquals(Money.of("5.00"), policy.discountFor(Money.of("40.00")));
    }

    @Test
    void aFixedAmountIsCappedAtTheSubtotal() {
        DiscountPolicy policy = DiscountPolicy.fixedOff(Money.of("50.00"));

        assertEquals(Money.of("30.00"), policy.discountFor(Money.of("30.00")));
    }

    @Test
    void aFixedAmountTakesNothingOffWhenItIsZero() {
        assertEquals(Money.ZERO, DiscountPolicy.fixedOff(Money.ZERO).discountFor(Money.of("12.34")));
    }

    @Test
    void rejectsAPercentageOutsideZeroToOneHundred() {
        assertThrows(IllegalArgumentException.class,
                () -> DiscountPolicy.percentageAbove(Money.ZERO, -1));
        assertThrows(IllegalArgumentException.class,
                () -> DiscountPolicy.percentageAbove(Money.ZERO, 101));
    }
}

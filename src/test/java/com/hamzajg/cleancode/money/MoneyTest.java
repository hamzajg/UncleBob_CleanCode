package com.hamzajg.cleancode.money;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoneyTest {

    @Test
    void parsesDecimalsAndWholeNumbers() {
        assertEquals(1999, Money.of("19.99").cents());
        assertEquals(700, Money.of("7").cents());
        assertEquals(0, Money.of("0.00").cents());
    }

    @Test
    void ignoresSurroundingWhitespace() {
        assertEquals(1250, Money.of("  12.50 ").cents());
    }

    @Test
    void roundsHalfUpToTheNearestCent() {
        assertEquals(1235, Money.of("12.345").cents());
        assertEquals(1234, Money.of("12.344").cents());
    }

    @Test
    void rejectsTextThatIsNotAnAmount() {
        assertThrows(IllegalArgumentException.class, () -> Money.of("twelve"));
        assertThrows(IllegalArgumentException.class, () -> Money.of(""));
        assertThrows(IllegalArgumentException.class, () -> Money.of("   "));
    }

    @Test
    void rejectsNegativeAmounts() {
        assertThrows(IllegalArgumentException.class, () -> Money.of("-1.00"));
    }

    @Test
    void addsSubtractsAndMultiplies() {
        assertEquals(Money.of("3.00"), Money.of("1.25").plus(Money.of("1.75")));
        assertEquals(Money.of("0.50"), Money.of("1.25").minus(Money.of("0.75")));
        assertEquals(Money.of("3.75"), Money.of("1.25").times(3));
    }

    @Test
    void subtractionCannotGoBelowZero() {
        assertThrows(IllegalArgumentException.class, () -> Money.of("1.00").minus(Money.of("2.00")));
    }

    @Test
    void rejectsNegativeMultipliers() {
        assertThrows(IllegalArgumentException.class, () -> Money.of("1.00").times(-1));
    }

    @Test
    void computesAPortionInBasisPoints() {
        assertEquals(Money.of("4.00"), Money.of("19.99").portion(2000));
        assertEquals(Money.ZERO, Money.of("19.99").portion(0));
        assertEquals(Money.of("19.99"), Money.of("19.99").portion(10_000));
    }

    @Test
    void rejectsBasisPointsOutsideZeroToOneHundredPercent() {
        assertThrows(IllegalArgumentException.class, () -> Money.of("1.00").portion(-1));
        assertThrows(IllegalArgumentException.class, () -> Money.of("1.00").portion(10_001));
    }

    @Test
    void comparesByAmount() {
        assertTrue(Money.of("2.00").isAtLeast(Money.of("2.00")));
        assertTrue(Money.of("2.01").isAtLeast(Money.of("2.00")));
        assertTrue(Money.of("1.99").compareTo(Money.of("2.00")) < 0);
    }

    @Test
    void formatsWithTwoDecimals() {
        assertEquals("19.99", Money.of("19.99").toString());
        assertEquals("0.00", Money.ZERO.toString());
        assertEquals("7.00", Money.of("7").toString());
    }
}

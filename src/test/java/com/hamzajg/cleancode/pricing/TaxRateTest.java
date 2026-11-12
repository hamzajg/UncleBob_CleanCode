package com.hamzajg.cleancode.pricing;

import com.hamzajg.cleancode.money.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaxRateTest {

    @Test
    void taxesTheGivenAmount() {
        assertEquals(Money.of("20.00"), TaxRate.ofPercent(20).taxOn(Money.of("100.00")));
        assertEquals(Money.ZERO, TaxRate.ofPercent(0).taxOn(Money.of("100.00")));
    }

    @Test
    void supportsFractionalRatesInBasisPoints() {
        TaxRate rate = new TaxRate(850);

        assertEquals(Money.of("1.70"), rate.taxOn(Money.of("19.99")));
        assertEquals("8.5%", rate.label());
    }

    @Test
    void labelsWholePercentagesWithoutADecimal() {
        assertEquals("20%", TaxRate.ofPercent(20).label());
        assertEquals("0%", TaxRate.ofPercent(0).label());
    }

    @Test
    void rejectsRatesOutsideZeroToOneHundredPercent() {
        assertThrows(IllegalArgumentException.class, () -> new TaxRate(-1));
        assertThrows(IllegalArgumentException.class, () -> new TaxRate(10_001));
        assertThrows(IllegalArgumentException.class, () -> TaxRate.ofPercent(101));
    }
}

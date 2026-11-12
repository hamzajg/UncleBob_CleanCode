package com.hamzajg.cleancode.checkout;

import com.hamzajg.cleancode.money.Money;
import com.hamzajg.cleancode.pricing.DiscountPolicy;
import com.hamzajg.cleancode.pricing.LineItem;
import com.hamzajg.cleancode.pricing.TaxRate;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReceiptPrinterTest {

    private final ReceiptPrinter printer = new ReceiptPrinter();

    @Test
    void printsTheHeaderItemsAndTotals() {
        Receipt receipt = new Checkout(
                DiscountPolicy.percentageAbove(Money.of("50.00"), 10),
                TaxRate.ofPercent(20))
                .checkout(List.of(
                        new LineItem("KB-01", "Mechanical keyboard", Money.of("79.90"), 1),
                        new LineItem("CB-02", "USB-C cable", Money.of("12.50"), 2)));

        String text = printer.print(receipt);

        assertTrue(text.startsWith("ARTICLE"), text);
        assertTrue(text.contains("Mechanical keyboard"), text);
        assertTrue(text.contains("USB-C cable"), text);
        assertTrue(text.contains("Subtotal"), text);
        assertTrue(text.contains("Discount"), text);
        assertTrue(text.contains("Tax (20%)"), text);
        assertTrue(text.contains("Total"), text);
        assertTrue(text.contains("113.29"), text);
    }

    @Test
    void omitsTheDiscountLineWhenNothingIsTakenOff() {
        Receipt receipt = Checkout.withDefaults()
                .checkout(List.of(new LineItem("CB-02", "USB-C cable", Money.of("12.50"), 1)));

        String text = printer.print(receipt);

        assertFalse(text.contains("Discount"), text);
        assertTrue(text.contains("Tax (20%)"), text);
        assertTrue(text.contains("15.00"), text);
    }

    @Test
    void abbreviatesNamesThatWouldBreakTheColumns() {
        Receipt receipt = Checkout.withDefaults()
                .checkout(List.of(new LineItem("LONG-01",
                        "An exceptionally long product name that cannot fit", Money.of("1.00"), 1)));

        String text = printer.print(receipt);

        assertTrue(text.contains("\u2026"), text);
        assertFalse(text.contains("cannot fit"), text);
    }
}

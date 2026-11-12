package com.hamzajg.cleancode.checkout;

import com.hamzajg.cleancode.money.Money;
import com.hamzajg.cleancode.pricing.DiscountPolicy;
import com.hamzajg.cleancode.pricing.LineItem;
import com.hamzajg.cleancode.pricing.TaxRate;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckoutTest {

    private final LineItem keyboard = new LineItem("KB-01", "Mechanical keyboard", Money.of("79.90"), 1);
    private final LineItem cable = new LineItem("CB-02", "USB-C cable", Money.of("12.50"), 2);

    @Test
    void pricesADiscountedCart() {
        Checkout checkout = new Checkout(
                DiscountPolicy.percentageAbove(Money.of("50.00"), 10),
                TaxRate.ofPercent(20));

        Receipt receipt = checkout.checkout(List.of(keyboard, cable));

        assertEquals(Money.of("104.90"), receipt.subtotal());
        assertEquals(Money.of("10.49"), receipt.discount());
        assertEquals(Money.of("18.88"), receipt.tax());
        assertEquals(Money.of("113.29"), receipt.total());
    }

    @Test
    void pricesACartBelowTheDiscountThreshold() {
        Checkout checkout = new Checkout(
                DiscountPolicy.percentageAbove(Money.of("50.00"), 10),
                TaxRate.ofPercent(20));

        Receipt receipt = checkout.checkout(List.of(cable));

        assertEquals(Money.of("25.00"), receipt.subtotal());
        assertEquals(Money.ZERO, receipt.discount());
        assertEquals(Money.of("5.00"), receipt.tax());
        assertEquals(Money.of("30.00"), receipt.total());
    }

    @Test
    void totalIsSubtotalMinusDiscountPlusTax() {
        Checkout checkout = Checkout.withDefaults();

        Receipt receipt = checkout.checkout(List.of(keyboard, cable));

        assertEquals(receipt.subtotal().minus(receipt.discount()).plus(receipt.tax()), receipt.total());
    }

    @Test
    void keepsOneLinePerItemInCartOrder() {
        Checkout checkout = Checkout.withDefaults();

        Receipt receipt = checkout.checkout(List.of(keyboard, cable));

        assertEquals(List.of(
                new Receipt.Line(keyboard, Money.of("79.90")),
                new Receipt.Line(cable, Money.of("25.00"))), receipt.lines());
    }

    @Test
    void doesNotExposeTheCartAsMutableState() {
        List<LineItem> cart = new ArrayList<>(List.of(cable));

        Receipt receipt = Checkout.withDefaults().checkout(cart);
        cart.clear();

        assertEquals(1, receipt.lines().size());
        assertThrows(UnsupportedOperationException.class,
                () -> receipt.lines().add(new Receipt.Line(keyboard, Money.of("79.90"))));
    }

    @Test
    void rejectsAnEmptyCart() {
        Checkout checkout = Checkout.withDefaults();

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> checkout.checkout(List.of()));
        assertTrue(error.getMessage().contains("empty"));
    }

    @Test
    void rejectsAMissingCart() {
        assertThrows(NullPointerException.class, () -> Checkout.withDefaults().checkout(null));
    }
}

package com.hamzajg.cleancode.pricing;

import com.hamzajg.cleancode.money.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LineItemTest {

    @Test
    void multipliesTheUnitPriceByTheQuantity() {
        LineItem item = new LineItem("KB-01", "Mechanical keyboard", Money.of("79.90"), 3);

        assertEquals(Money.of("239.70"), item.subtotal());
    }

    @Test
    void rejectsANonPositiveQuantity() {
        assertThrows(IllegalArgumentException.class,
                () -> new LineItem("KB-01", "Keyboard", Money.of("1.00"), 0));
        assertThrows(IllegalArgumentException.class,
                () -> new LineItem("KB-01", "Keyboard", Money.of("1.00"), -1));
    }

    @Test
    void rejectsBlankText() {
        assertThrows(IllegalArgumentException.class,
                () -> new LineItem(" ", "Keyboard", Money.of("1.00"), 1));
        assertThrows(IllegalArgumentException.class,
                () -> new LineItem("KB-01", "", Money.of("1.00"), 1));
    }

    @Test
    void rejectsAMissingPrice() {
        assertThrows(NullPointerException.class,
                () -> new LineItem("KB-01", "Keyboard", null, 1));
    }
}

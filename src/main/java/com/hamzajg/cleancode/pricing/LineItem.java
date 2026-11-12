package com.hamzajg.cleancode.pricing;

import com.hamzajg.cleancode.money.Money;

import java.util.Objects;

/**
 * One product line in a cart.
 *
 * <p>The canonical constructor rejects every state the rest of the code should
 * never have to defend against, so {@link #subtotal()} cannot fail.
 */
public record LineItem(String sku, String name, Money unitPrice, int quantity) {

    public LineItem {
        sku = requireText(sku, "sku");
        name = requireText(name, "name");
        Objects.requireNonNull(unitPrice, "unitPrice");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive: " + quantity);
        }
    }

    public Money subtotal() {
        return unitPrice.times(quantity);
    }

    private static String requireText(String value, String field) {
        Objects.requireNonNull(value, field);
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " cannot be blank");
        }
        return value;
    }
}

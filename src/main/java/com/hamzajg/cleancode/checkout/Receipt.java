package com.hamzajg.cleancode.checkout;

import com.hamzajg.cleancode.money.Money;
import com.hamzajg.cleancode.pricing.LineItem;
import com.hamzajg.cleancode.pricing.TaxRate;

import java.util.List;
import java.util.Objects;

/**
 * The priced outcome of a checkout: one line per cart item, then the totals.
 *
 * <p>A receipt is a snapshot, so it copies the lines it is given and never
 * exposes a mutable list.
 */
public record Receipt(
        List<Line> lines,
        Money subtotal,
        Money discount,
        Money tax,
        TaxRate taxRate,
        Money total) {

    public Receipt {
        lines = List.copyOf(lines);
        Objects.requireNonNull(subtotal, "subtotal");
        Objects.requireNonNull(discount, "discount");
        Objects.requireNonNull(tax, "tax");
        Objects.requireNonNull(taxRate, "taxRate");
        Objects.requireNonNull(total, "total");
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }

    /** One cart item with the amount it contributes to the subtotal. */
    public record Line(LineItem item, Money amount) {
        public Line {
            Objects.requireNonNull(item, "item");
            Objects.requireNonNull(amount, "amount");
        }
    }
}

package com.hamzajg.cleancode.checkout;

import com.hamzajg.cleancode.money.Money;
import com.hamzajg.cleancode.pricing.DiscountPolicy;
import com.hamzajg.cleancode.pricing.LineItem;
import com.hamzajg.cleancode.pricing.TaxRate;

import java.util.List;
import java.util.Objects;

/**
 * Prices a cart: subtotal, discount, tax on the discounted amount, total.
 *
 * <p>The method reads top to bottom as the sentence it implements, and each
 * step is a single expression over values that are already valid.
 */
public final class Checkout {

    private final DiscountPolicy discountPolicy;
    private final TaxRate taxRate;

    public Checkout(DiscountPolicy discountPolicy, TaxRate taxRate) {
        this.discountPolicy = Objects.requireNonNull(discountPolicy, "discountPolicy");
        this.taxRate = Objects.requireNonNull(taxRate, "taxRate");
    }

    /** A checkout with no discount and the 20% rate used by the demo. */
    public static Checkout withDefaults() {
        return new Checkout(DiscountPolicy.none(), TaxRate.ofPercent(20));
    }

    public Receipt checkout(List<LineItem> items) {
        Objects.requireNonNull(items, "items");
        if (items.isEmpty()) {
            throw new IllegalArgumentException("cannot check out an empty cart");
        }

        List<Receipt.Line> lines = items.stream()
                .map(item -> new Receipt.Line(item, item.subtotal()))
                .toList();

        Money subtotal = lines.stream()
                .map(Receipt.Line::amount)
                .reduce(Money.ZERO, Money::plus);
        Money discount = discountPolicy.discountFor(subtotal);
        Money taxable = subtotal.minus(discount);
        Money tax = taxRate.taxOn(taxable);

        return new Receipt(lines, subtotal, discount, tax, taxRate, taxable.plus(tax));
    }
}

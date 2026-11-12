package com.hamzajg.cleancode;

import com.hamzajg.cleancode.checkout.Checkout;
import com.hamzajg.cleancode.checkout.ReceiptPrinter;
import com.hamzajg.cleancode.money.Money;
import com.hamzajg.cleancode.pricing.DiscountPolicy;
import com.hamzajg.cleancode.pricing.LineItem;
import com.hamzajg.cleancode.pricing.TaxRate;

import java.util.List;

/** Prints one receipt, so the pieces can be seen working together. */
public final class Demo {

    public static void main(String[] args) {
        Checkout checkout = new Checkout(
                DiscountPolicy.percentageAbove(Money.of("50.00"), 10),
                TaxRate.ofPercent(20));

        List<LineItem> cart = List.of(
                new LineItem("KB-01", "Mechanical keyboard", Money.of("79.90"), 1),
                new LineItem("CB-02", "USB-C cable", Money.of("12.50"), 2));

        System.out.print(new ReceiptPrinter().print(checkout.checkout(cart)));
    }
}

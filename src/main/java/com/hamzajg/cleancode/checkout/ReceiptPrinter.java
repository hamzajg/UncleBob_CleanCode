package com.hamzajg.cleancode.checkout;

import com.hamzajg.cleancode.money.Money;

/**
 * Renders a {@link Receipt} as plain text.
 *
 * <p>Formatting lives here, not on the receipt: the receipt is a value, and a
 * value should not know about column widths or terminals.
 */
public final class ReceiptPrinter {

    private static final int NAME_WIDTH = 30;
    private static final int RULE_WIDTH = 46;

    public String print(Receipt receipt) {
        StringBuilder out = new StringBuilder();
        out.append(String.format("%-" + NAME_WIDTH + "s %4s %10s%n", "ARTICLE", "QTY", "AMOUNT"));

        for (Receipt.Line line : receipt.lines()) {
            out.append(String.format("%-" + NAME_WIDTH + "s %4d %10s%n",
                    abbreviate(line.item().name()), line.item().quantity(), line.amount()));
        }

        out.append("-".repeat(RULE_WIDTH)).append(System.lineSeparator());
        out.append(summary("Subtotal", receipt.subtotal().toString()));
        if (!receipt.discount().equals(Money.ZERO)) {
            out.append(summary("Discount", "-" + receipt.discount()));
        }
        out.append(summary("Tax (" + receipt.taxRate().label() + ")", receipt.tax().toString()));
        out.append("-".repeat(RULE_WIDTH)).append(System.lineSeparator());
        out.append(summary("Total", receipt.total().toString()));
        return out.toString();
    }

    private static String summary(String label, String amount) {
        return String.format("%-" + (RULE_WIDTH - 10) + "s %10s%n", label, amount);
    }

    private static String abbreviate(String name) {
        return name.length() <= NAME_WIDTH ? name : name.substring(0, NAME_WIDTH - 1) + "\u2026";
    }
}

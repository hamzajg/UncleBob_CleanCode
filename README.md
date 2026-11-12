# UncleBob_CleanCode

A small, tested Java project that puts the practices from *Clean Code* into
code you can run and read. It is not a summary of the book: every section
below points at a file in this repository where the practice is applied, and
at the test that holds it in place.

The example domain is a checkout: price a cart, apply one discount rule, add
sales tax, print a receipt. It is deliberately small, because the point is how
the code is shaped, not what it does.

```
$ mvn -q exec:java
ARTICLE                         QTY     AMOUNT
Mechanical keyboard               1      79.90
USB-C cable                       2      25.00
----------------------------------------------
Subtotal                                 104.90
Discount                                 -10.49
Tax (20%)                                 18.88
----------------------------------------------
Total                                    113.29
```

## Run it

Requires JDK 17 and Maven.

```bash
mvn test        # 39 tests
mvn exec:java   # print the receipt above
```

## The practices, and where to find them

### Intention-revealing names

`Money`, `LineItem`, `DiscountPolicy`, `TaxRate`, `Receipt`. A name says what
the thing *is*, not what it holds: `LineItem.quantity` rather than
`nb`, `subtotal()` rather than `calc2()`.

The alternative is the comment that has to explain the name:

```java
// before
int q;                  // quantity of the article
BigDecimal pu;          // unit price

// after
int quantity;
Money unitPrice;
```

### Small functions that do one thing

`Checkout.checkout` is five statements: build the lines, sum them, apply the
discount, tax the discounted amount, assemble the receipt. Each statement is
one step of the sentence in the Javadoc, and each step delegates.

`ReceiptPrinter` exists so that `Receipt` does not have to do two jobs
(hold the values and know about column widths).

### Objects that cannot be invalid

Every value validates itself in its constructor, so no caller has to:

```java
public record LineItem(String sku, String name, Money unitPrice, int quantity) {
    public LineItem {
        sku = requireText(sku, "sku");
        name = requireText(name, "name");
        Objects.requireNonNull(unitPrice, "unitPrice");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive: " + quantity);
        }
    }
}
```

After that, `subtotal()` is a single expression with no failure path, and the
tests for "quantity must be positive" live next to the rule instead of in every
caller. The same applies to `Money` (never negative), `TaxRate` (0–100%) and
`DiscountPolicy` (0–100%).

### Errors as exceptions, not as return values

`Money.minus` throws rather than returning a negative amount, and
`Checkout.checkout` throws `IllegalArgumentException("cannot check out an empty
cart")` rather than returning a receipt of zeros that the caller must
distinguish from a real empty total. The message says what the caller did
wrong, and `CheckoutTest.rejectsAnEmptyCart` asserts it.

### Comments only where the code cannot say it

There are comments in this project, but they explain *why*, not *what*:
`Money` stores cents "to avoid the rounding drift of `double` arithmetic";
`TaxRate` uses basis points "so that rates such as 8.5% are exact"; `Checkout`
takes tax on the discounted amount because that is the rule. Nothing comments
a line like `i++`.

### Money is not a `double`

`Money` stores an integer number of cents and does all arithmetic on it.
`Money.of("12.345")` rounds half up to 12.35 once, at the boundary, instead of
letting binary floating point accumulate error across a cart. Rounding is
tested explicitly (`MoneyTest.roundsHalfUpToTheNearestCent`,
`DiscountPolicyTest.roundsTheDiscountToTheNearestCent`).

### Tests are first-class

39 JUnit 5 tests, one behaviour each, named for the rule they check. They are
grouped by the class they test, and the checkout tests are written from the
outside — `pricesADiscountedCart` asserts the four numbers a user would look
at, not the internals that produced them.

The suite is also the specification: `CheckoutTest.totalIsSubtotalMinusDiscountPlusTax`
states the arithmetic identity, and `CheckoutTest.doesNotExposeTheCartAsMutableState`
pins the snapshot behaviour of `Receipt`.

## Layout

```
src/main/java/com/hamzajg/cleancode/
  money/Money.java               integer cents, validated, immutable
  pricing/LineItem.java          a cart line; rejects bad quantities
  pricing/DiscountPolicy.java    one named rule: a percentage above a threshold,
                                 or a fixed amount capped at the subtotal
  pricing/TaxRate.java           basis points, exact for 8.5%
  checkout/Checkout.java         cart -> receipt, one statement per step
  checkout/Receipt.java          immutable snapshot of the result
  checkout/ReceiptPrinter.java   the only class that knows about text layout
  Demo.java                      prints one receipt
src/test/java/...                one test class per production class
```

## Continuous integration

[`.github/workflows/ci.yml`](.github/workflows/ci.yml) runs `mvn -B verify` on
JDK 17 for every push and pull request to `master`.

## License

MIT — see [LICENSE](LICENSE).

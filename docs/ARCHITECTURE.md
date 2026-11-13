# Architecture — UncleBob_CleanCode

Current-state map. Descriptive, not aspirational; see `docs/adr/` for why.

## Shape

```
UncleBob_CleanCode/           Maven single-module (Java 17)
├── src/main/java/com/hamzajg/cleancode/
│   ├── Demo.java                    entry — builds cart, prints receipt
│   ├── money/Money.java             immutable money value object
│   ├── pricing/
│   │   ├── LineItem.java            priced line (qty × price)
│   │   ├── DiscountPolicy.java      single discount rule
│   │   └── TaxRate.java             tax rate + calculation
│   └── checkout/
│       ├── Checkout.java            orchestrates pricing pipeline
│       ├── Receipt.java             receipt model
│       └── ReceiptPrinter.java      ASCII receipt renderer
├── src/test/java/com/hamzajg/cleancode/
│   ├── money/MoneyTest.java
│   ├── pricing/{LineItemTest,DiscountPolicyTest,TaxRateTest}.java
│   └── checkout/{CheckoutTest,ReceiptPrinterTest}.java
└── pom.xml                    release 17, junit-jupiter, exec plugin
```

## Pipeline

`Demo` builds `LineItem`s → `Checkout` applies `DiscountPolicy` +
`TaxRate` (both over `Money`) → `Receipt` → `ReceiptPrinter` renders
ASCII. Each step is a small single-purpose class; `Money` is immutable
and owns all arithmetic.

## Dependency direction

`Demo` → `Checkout` → `pricing`/`checkout` → `Money`. No framework, no
DI — plain constructor wiring in `Demo`.

## Testing

JUnit 5 tests mirror `src/main` packages — one test class per unit.
`mvn -B verify` is the gate; `mvn -q exec:java` runs the demo.

## State

None — pure computation, no I/O beyond console output.

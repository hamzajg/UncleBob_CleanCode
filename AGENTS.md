# AGENTS.md — UncleBob_CleanCode

Guidance for AI agents (and humans) working in this repository.

## Project

A small, tested Java project applying *Clean Code* practices to runnable
code — not a book summary: every section points at a file where the
practice is applied and the test that holds it. Domain: a checkout —
price a cart, apply one discount rule, add sales tax, print a receipt.
Deliberately small; the point is code shape, not features.
Stack: Java 17 (`maven.compiler.release`), Maven, JUnit 5 (Jupiter),
exec-maven-plugin for the demo.

## Commands

Verified 2026-11-13 against CI (`.github/workflows/ci.yml`) and `pom.xml`.

| Task | Command |
|---|---|
| Verify (CI) | `mvn -B verify` |
| Run demo | `mvn -q exec:java` (prints a priced receipt) |
| Test only | `mvn -B test` |

Requires: JDK 17+ and Maven. `target/` is build output — gitignored
normally.

## Architecture

```
src/main/java/com/hamzajg/cleancode/
  Demo.java                    entry point — builds cart, prints receipt
  money/Money.java             value object (immutable money)
  pricing/
    LineItem.java              priced line
    DiscountPolicy.java        single discount rule
    TaxRate.java               tax calculation
  checkout/
    Checkout.java              orchestrates price → discount → tax
    Receipt.java               receipt model
    ReceiptPrinter.java        ASCII receipt output
src/test/java/com/hamzajg/cleancode/
  money/MoneyTest.java
  pricing/{LineItemTest,DiscountPolicyTest,TaxRateTest}.java
  checkout/{CheckoutTest,ReceiptPrinterTest}.java
pom.xml                        Java 17, junit-jupiter, exec plugin
```

Design intent: immutable value objects (`Money`), single-responsibility
classes, dependency direction `Demo → Checkout → {LineItem, Discount,
Tax} → Money`, tests mirror main packages.

## Conventions

- **This repo teaches Clean Code** — every change must keep the
  practices visible: small classes, clear names, immutability, tests
  that document behavior. A "clever" refactor that obscures the lesson
  is a regression.
- JUnit 5 (Jupiter); tests are the spec — keep them readable.
- Money math via `Money` value object — don't introduce `double`/`float`
  arithmetic.
- KISS: the domain is intentionally tiny; don't add features.

## Landmines

- **Default branch is `master`, not `main`.**
- Java **17** (`maven.compiler.release`) — not older.
- `target/` exists locally — build output, don't commit.
- The example domain is fixed (checkout) — extending it changes the
  teaching example; prefer new files over mutating the canonical ones.
- exec:java runs `Demo` — it's a demo, not a service.

## Agent rules

- Branch from `master`; name branches `feat/*`, `fix/*`, `chore/*`, `docs/*`.
- Docs-only changes still go through a PR; never commit to `master` directly.
- Verify before claiming: `mvn -B verify` and `mvn -q exec:java` —
  report real output.
- Preserve the Clean Code lessons — changes must make the code *more*
  readable/testable, not less.
- Keep changes minimal and consistent with surrounding code. No drive-by
  refactors.
- Update AGENTS.md / docs/ARCHITECTURE.md when the domain or structure
  changes.

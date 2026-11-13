# ADR 0001 — Initial architecture

Date: 2026-11-13 (baseline recorded retroactively)
Status: accepted (describes what is built)

## Context

A teaching repo applying Clean Code practices to a runnable checkout
domain — the architecture *is* the lesson.

## Decisions

1. **Tiny checkout domain.** Cart pricing: line items, one discount
   rule, tax, ASCII receipt. Rationale: small enough to read end-to-end;
   the shape of the code is the subject.
2. **Immutable `Money` value object.** All arithmetic lives in `Money`;
   no `double`/`float` in domain math. Rationale: money correctness +
   demonstrates value objects.
3. **Single-responsibility pipeline.** `LineItem` → `DiscountPolicy` +
   `TaxRate` → `Checkout` → `Receipt` → `ReceiptPrinter`, each one job.
   Rationale: Clean Code's SRP made concrete.
4. **Plain Java, constructor wiring.** No Spring/DI — `Demo` wires
   objects directly. Rationale: dependencies are visible; framework
   noise would obscure the lesson.
5. **Tests as executable spec.** JUnit 5 per class, mirroring packages.
   Rationale: tests document intended behavior — the Clean Code way.
6. **Java 17 + Maven.** Modern LTS, standard build. Rationale: current
   toolchain without build complexity.

## Consequences / open questions

- The domain is intentionally closed — extending it is a teaching
  decision, not a feature request.
- No persistence/UI — out of scope by design.
- Any change must keep practices *visible*: if a refactor makes the
  lesson harder to see, it's wrong for this repo even if "better"
  engineering.

package variant1.banking.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value Object: сума операції, яка за визначенням строго додатна.
 * Guard Clause живе в одному місці (fail-fast), тому deposit/withdraw
 * не повторюють перевірку "amount > 0" (DRY).
 */
public record PositiveAmount(BigDecimal value) {

    public PositiveAmount {
        Objects.requireNonNull(value, "value");
        if (value.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    public static PositiveAmount of(String value) {
        return new PositiveAmount(new BigDecimal(value));
    }
}

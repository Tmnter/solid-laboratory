package variant1.payment.model;

import java.math.BigDecimal;
import java.util.Objects;

/** Незмінний запит на платіж із перевіркою інваріантів (fail-fast). */
public class PaymentRequest {
    private final String transactionId;
    private final BigDecimal amount;
    private final String currency;
    private final String payerId;

    public PaymentRequest(String transactionId, BigDecimal amount, String currency, String payerId) {
        this.transactionId = Objects.requireNonNull(transactionId, "transactionId");
        this.amount = Objects.requireNonNull(amount, "amount");
        this.currency = Objects.requireNonNull(currency, "currency");
        this.payerId = Objects.requireNonNull(payerId, "payerId");
        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("Сума платежу має бути більше 0.");
        }
    }

    public String getTransactionId() { return transactionId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getPayerId() { return payerId; }
}

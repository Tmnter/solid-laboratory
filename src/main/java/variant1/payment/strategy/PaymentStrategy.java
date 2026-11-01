package variant1.payment.strategy;

import variant1.payment.model.PaymentRequest;

import java.math.BigDecimal;

/**
 * Точка розширення (Extension Point): кожен спосіб оплати реалізує цей інтерфейс.
 * Новий спосіб оплати додається новим класом, без змін у PaymentProcessor.
 */
public interface PaymentStrategy {

    /** Унікальний код способу оплати, наприклад "CREDIT_CARD". */
    String methodId();

    /** Комісія для вказаної суми, округлена до 2 знаків. */
    BigDecimal calculateFee(BigDecimal amount);

    /** Проведення платежу. totalAmount = сума + комісія (обчислює PaymentProcessor). */
    boolean process(PaymentRequest request, BigDecimal fee, BigDecimal totalAmount);
}

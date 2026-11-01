package variant1.payment.strategy;

import variant1.payment.model.PaymentRequest;

import java.math.BigDecimal;

public class BankTransferPaymentStrategy implements PaymentStrategy {
    private static final BigDecimal FIXED_FEE = new BigDecimal("5.00");

    @Override
    public String methodId() {
        return "BANK_TRANSFER";
    }

    /** Фіксовані $5.00 незалежно від суми. */
    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        return FIXED_FEE;
    }

    @Override
    public boolean process(PaymentRequest request, BigDecimal fee, BigDecimal totalAmount) {
        System.out.println("[BankTransfer] Формування платіжного доручення SWIFT/IBAN для "
                + request.getPayerId() + ". Сума: $" + totalAmount);
        return true;
    }
}

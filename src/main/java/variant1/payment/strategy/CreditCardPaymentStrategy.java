package variant1.payment.strategy;

import variant1.payment.model.PaymentRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CreditCardPaymentStrategy implements PaymentStrategy {
    private static final BigDecimal PERCENT = new BigDecimal("0.025");
    private static final BigDecimal FIXED = new BigDecimal("0.30");

    @Override
    public String methodId() {
        return "CREDIT_CARD";
    }

    /** 2.5% + $0.30 */
    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        return amount.multiply(PERCENT).add(FIXED).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean process(PaymentRequest request, BigDecimal fee, BigDecimal totalAmount) {
        System.out.println("[CreditCard] Авторизація картки для користувача " + request.getPayerId()
                + ". Списано: $" + totalAmount);
        return true;
    }
}

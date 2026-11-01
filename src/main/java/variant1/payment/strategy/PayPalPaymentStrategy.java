package variant1.payment.strategy;

import variant1.payment.model.PaymentRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PayPalPaymentStrategy implements PaymentStrategy {
    private static final BigDecimal PERCENT = new BigDecimal("0.034");

    @Override
    public String methodId() {
        return "PAYPAL";
    }

    /** 3.4% */
    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        return amount.multiply(PERCENT).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean process(PaymentRequest request, BigDecimal fee, BigDecimal totalAmount) {
        System.out.println("[PayPal] Перенаправлення на шлюз PayPal для " + request.getPayerId()
                + ". Комісія системи: $" + fee);
        return true;
    }
}

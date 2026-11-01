package variant1.payment.strategy;

import variant1.payment.model.PaymentRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CryptoPaymentStrategy implements PaymentStrategy {
    private static final BigDecimal PERCENT = new BigDecimal("0.008");
    private static final BigDecimal MIN_FEE = new BigDecimal("1.00");
    private static final BigDecimal MAX_FEE = new BigDecimal("20.00");

    @Override
    public String methodId() {
        return "CRYPTO";
    }

    /** 0.8%, але не менше $1.00 і не більше $20.00. */
    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        BigDecimal fee = amount.multiply(PERCENT).setScale(2, RoundingMode.HALF_UP);
        return fee.max(MIN_FEE).min(MAX_FEE);
    }

    @Override
    public boolean process(PaymentRequest request, BigDecimal fee, BigDecimal totalAmount) {
        System.out.println("[Crypto] Очікування підтвердження транзакції в мережі Blockchain для "
                + request.getPayerId() + ".");
        return true;
    }
}

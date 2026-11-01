package variant1.payment;

import org.junit.jupiter.api.Test;
import variant1.payment.model.PaymentRequest;
import variant1.payment.strategy.BankTransferPaymentStrategy;
import variant1.payment.strategy.CreditCardPaymentStrategy;
import variant1.payment.strategy.CryptoPaymentStrategy;
import variant1.payment.strategy.PayPalPaymentStrategy;
import variant1.payment.strategy.PaymentStrategy;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentProcessorTest {

    /** Нова стратегія, додана БЕЗ зміни жодного рядка в main-коді (демонстрація OCP). */
    static class ApplePayPaymentStrategy implements PaymentStrategy {
        @Override public String methodId() { return "APPLE_PAY"; }
        @Override public BigDecimal calculateFee(BigDecimal amount) { return new BigDecimal("1.50"); }
        @Override public boolean process(PaymentRequest r, BigDecimal fee, BigDecimal total) { return true; }
    }

    private static PaymentProcessor processor() {
        return new PaymentProcessor(List.of(
                new CreditCardPaymentStrategy(), new PayPalPaymentStrategy(),
                new BankTransferPaymentStrategy(), new CryptoPaymentStrategy()));
    }

    private static BigDecimal bd(String v) { return new BigDecimal(v); }

    @Test
    void creditCardFee() {
        assertEquals(bd("25.30"), processor().calculateProcessingFee(bd("1000"), "CREDIT_CARD"));
    }

    @Test
    void paypalFee() {
        assertEquals(bd("34.00"), processor().calculateProcessingFee(bd("1000"), "PAYPAL"));
    }

    @Test
    void bankTransferFeeIsFixed() {
        assertEquals(bd("5.00"), processor().calculateProcessingFee(bd("10"), "BANK_TRANSFER"));
        assertEquals(bd("5.00"), processor().calculateProcessingFee(bd("100000"), "BANK_TRANSFER"));
    }

    @Test
    void cryptoFeeIsClamped() {
        PaymentProcessor p = processor();
        assertEquals(bd("1.00"), p.calculateProcessingFee(bd("10"), "CRYPTO"));
        assertEquals(bd("8.00"), p.calculateProcessingFee(bd("1000"), "CRYPTO"));
        assertEquals(bd("20.00"), p.calculateProcessingFee(bd("100000"), "CRYPTO"));
    }

    @Test
    void processPaymentSucceedsForAllMethods() {
        PaymentRequest req = new PaymentRequest("T1", bd("100"), "USD", "u1");
        for (String id : List.of("CREDIT_CARD", "PAYPAL", "BANK_TRANSFER", "CRYPTO")) {
            assertTrue(processor().processPayment(req, id), id);
        }
    }

    @Test
    void unknownMethodIsRejected() {
        PaymentRequest req = new PaymentRequest("T1", bd("100"), "USD", "u1");
        assertThrows(IllegalArgumentException.class, () -> processor().processPayment(req, "APPLE_PAY"));
    }

    @Test
    void newStrategyPluggedInWithoutChangingProcessor() {
        PaymentProcessor p = new PaymentProcessor(List.of(new CreditCardPaymentStrategy(), new ApplePayPaymentStrategy()));
        PaymentRequest req = new PaymentRequest("T2", bd("100"), "USD", "u1");
        assertEquals(bd("1.50"), p.calculateProcessingFee(bd("100"), "APPLE_PAY"));
        assertTrue(p.processPayment(req, "APPLE_PAY"));
    }

    @Test
    void duplicateStrategyIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new PaymentProcessor(List.of(new PayPalPaymentStrategy(), new PayPalPaymentStrategy())));
    }

    @Test
    void paymentRequestGuardsInvariants() {
        assertThrows(IllegalArgumentException.class, () -> new PaymentRequest("T", bd("0"), "USD", "u"));
        assertThrows(NullPointerException.class, () -> new PaymentRequest(null, bd("1"), "USD", "u"));
    }
}

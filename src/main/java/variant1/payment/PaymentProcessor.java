package variant1.payment;

import variant1.payment.model.PaymentRequest;
import variant1.payment.strategy.PaymentStrategy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Закритий для змін, відкритий для розширення: не містить switch/if за типом платежу.
 * Нові способи оплати підключаються передачею ще однієї стратегії в конструктор.
 */
public class PaymentProcessor {
    private final Map<String, PaymentStrategy> strategies = new HashMap<>();

    public PaymentProcessor(List<PaymentStrategy> strategyList) {
        for (PaymentStrategy strategy : strategyList) {
            if (strategies.putIfAbsent(strategy.methodId(), strategy) != null) {
                throw new IllegalArgumentException("Дублікат способу оплати: " + strategy.methodId());
            }
        }
    }

    public BigDecimal calculateProcessingFee(BigDecimal amount, String methodId) {
        return findStrategy(methodId).calculateFee(amount);
    }

    public boolean processPayment(PaymentRequest request, String methodId) {
        PaymentStrategy strategy = findStrategy(methodId);

        System.out.println("[LOG] Початок обробки транзакції " + request.getTransactionId()
                + " на суму " + request.getAmount() + " " + request.getCurrency());

        BigDecimal fee = strategy.calculateFee(request.getAmount());
        BigDecimal totalAmount = request.getAmount().add(fee);
        return strategy.process(request, fee, totalAmount);
    }

    private PaymentStrategy findStrategy(String methodId) {
        PaymentStrategy strategy = strategies.get(methodId);
        if (strategy == null) {
            throw new IllegalArgumentException("Непідтримуваний тип платежу: " + methodId);
        }
        return strategy;
    }
}

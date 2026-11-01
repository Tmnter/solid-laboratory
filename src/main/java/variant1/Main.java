package variant1;

import variant1.banking.model.CurrentAccount;
import variant1.banking.model.FixedDepositAccount;
import variant1.banking.model.PositiveAmount;
import variant1.banking.service.BankService;
import variant1.order.OrderProcessor;
import variant1.order.model.OrderItem;
import variant1.order.repository.FileOrderRepository;
import variant1.order.repository.OrderRepository;
import variant1.order.service.EmailNotificationService;
import variant1.order.service.HtmlInvoiceGenerator;
import variant1.order.service.InvoiceGenerator;
import variant1.order.service.NotificationService;
import variant1.order.service.OrderCalculator;
import variant1.order.service.OrderValidator;
import variant1.payment.PaymentProcessor;
import variant1.payment.model.PaymentRequest;
import variant1.payment.strategy.BankTransferPaymentStrategy;
import variant1.payment.strategy.CreditCardPaymentStrategy;
import variant1.payment.strategy.CryptoPaymentStrategy;
import variant1.payment.strategy.PayPalPaymentStrategy;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.Clock;
import java.util.List;

/** Composition Root: єдине місце, де створюються конкретні об'єкти та з'єднуються залежності. */
public class Main {

    public static void main(String[] args) {
        runOrderDemo();
        System.out.println();
        runPaymentDemo();
        System.out.println();
        runBankingDemo();
    }

    private static void runBankingDemo() {
        CurrentAccount current = new CurrentAccount("UA-001", new BigDecimal("1000.00"));
        FixedDepositAccount deposit = new FixedDepositAccount("UA-002", new BigDecimal("5000.00"), 12);

        BankService bankService = new BankService();
        bankService.processMonthlyFee(current, PositiveAmount.of("25.00"));

        deposit.deposit(PositiveAmount.of("100.00"));
        // bankService.processMonthlyFee(deposit, ...); // не компілюється: депозит не WithdrawableAccount
        System.out.println("Депозит " + deposit.getAccountNumber() + ": " + deposit.getBalance());
    }

    private static void runOrderDemo() {
        InvoiceGenerator invoiceGenerator = new HtmlInvoiceGenerator();
        OrderRepository repository = new FileOrderRepository(Path.of("orders_db.txt"), Clock.systemDefaultZone());
        NotificationService notifications = new EmailNotificationService("smtp.store.local:587", System.out);

        OrderProcessor orderProcessor = new OrderProcessor(
                new OrderValidator(), new OrderCalculator(), invoiceGenerator, repository, notifications);

        List<OrderItem> items = List.of(
                new OrderItem("1", "Ноутбук", new BigDecimal("30000.00"), 1),
                new OrderItem("2", "Мишка", new BigDecimal("500.00"), 2));

        orderProcessor.processOrder("client@example.com", "VIP", items, "Чернівці, вул. Головна, 1");
    }

    private static void runPaymentDemo() {
        PaymentProcessor paymentProcessor = new PaymentProcessor(List.of(
                new CreditCardPaymentStrategy(),
                new PayPalPaymentStrategy(),
                new BankTransferPaymentStrategy(),
                new CryptoPaymentStrategy()));

        PaymentRequest request = new PaymentRequest("TX-1001", new BigDecimal("250.00"), "USD", "user-42");
        paymentProcessor.processPayment(request, "CREDIT_CARD");
        paymentProcessor.processPayment(request, "CRYPTO");
    }
}

package variant1.order;

import variant1.order.model.CustomerCategory;
import variant1.order.model.OrderCalculationResult;
import variant1.order.model.OrderItem;
import variant1.order.repository.OrderRepository;
import variant1.order.service.InvoiceGenerator;
import variant1.order.service.NotificationService;
import variant1.order.service.OrderCalculator;
import variant1.order.service.OrderValidator;

import java.util.List;

/**
 * Оркестратор: лише викликає спеціалізовані сервіси у потрібному порядку.
 * Залежності передаються через конструктор.
 */
public class OrderProcessor {
    private static final String CONFIRMATION_SUBJECT = "Ваше замовлення успішно оформлено";

    private final OrderValidator validator;
    private final OrderCalculator calculator;
    private final InvoiceGenerator invoiceGenerator;
    private final OrderRepository repository;
    private final NotificationService notificationService;

    public OrderProcessor(OrderValidator validator, OrderCalculator calculator,
                          InvoiceGenerator invoiceGenerator, OrderRepository repository,
                          NotificationService notificationService) {
        this.validator = validator;
        this.calculator = calculator;
        this.invoiceGenerator = invoiceGenerator;
        this.repository = repository;
        this.notificationService = notificationService;
    }

    public void processOrder(String customerEmail, String customerCategory,
                             List<OrderItem> items, String deliveryAddress) {
        validator.validate(customerEmail, items);

        CustomerCategory category = CustomerCategory.fromString(customerCategory);
        OrderCalculationResult result = calculator.calculate(items, category);
        String invoice = invoiceGenerator.generate(customerEmail, deliveryAddress, items, result);

        // Якщо замовлення не збережене, виняток піднімається вище, підтвердження не надсилається.
        repository.save(customerEmail, result.getTotalAmount(), deliveryAddress);

        try {
            notificationService.send(customerEmail, CONFIRMATION_SUBJECT, invoice);
        } catch (RuntimeException ex) {
            // Замовлення вже збережене, тому збій сповіщення не скасовує його.
            System.err.println("Помилка відправки email: " + ex.getMessage());
        }
    }
}

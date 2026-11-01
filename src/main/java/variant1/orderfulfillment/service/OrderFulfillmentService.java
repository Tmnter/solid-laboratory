package variant1.orderfulfillment.service;

import variant1.orderfulfillment.model.Order;
import variant1.orderfulfillment.repository.IOrderRepository;

public class OrderFulfillmentService {
    private final IOrderRepository databaseRepository;
    private final IEmailService emailService;
    private final IAuditLogger fileLogger;

    public OrderFulfillmentService(IOrderRepository databaseRepository, IEmailService emailService, IAuditLogger fileLogger) {
        if (databaseRepository == null || emailService == null || fileLogger == null) {
            throw new IllegalArgumentException("Залежності не можуть бути null.");
        }
        this.databaseRepository = databaseRepository;
        this.emailService = emailService;
        this.fileLogger = fileLogger;
    }

    public void processOrder(Order order) {
        if (order == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Замовлення не може бути порожнім.");
        }

        this.databaseRepository.saveOrder(order);
        this.fileLogger.logTransaction("Успішно оброблено замовлення " + order.getOrderId());
        this.emailService.sendOrderConfirmation(order.getCustomerEmail(), order.getOrderId());
    }
}
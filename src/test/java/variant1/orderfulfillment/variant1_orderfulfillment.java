package variant1.orderfulfillment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Variant1_OrderFulfillmentRefactored {

    public static class Order {
        private String orderId;
        private String customerEmail;
        private double totalAmount;
        private List<String> items = new ArrayList<>();

        public String getOrderId() { return orderId; }
        public void setOrderId(String orderId) { this.orderId = orderId; }
        public String getCustomerEmail() { return customerEmail; }
        public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
        public double getTotalAmount() { return totalAmount; }
        public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
        public List<String> getItems() { return items; }
        public void setItems(List<String> items) { this.items = items; }
    }

    // 1. Виділення абстракцій (інтерфейсів) для низькорівневих сервісів
    public interface IOrderRepository {
        void saveOrder(Order order);
    }

    public interface IEmailService {
        void sendOrderConfirmation(String email, String orderId);
    }

    public interface IAuditLogger {
        void logTransaction(String message);
    }

    // 2. Конкретні реалізації інфраструктурних інтерфейсів
    public static class MySqlDatabaseRepository implements IOrderRepository {
        @Override
        public void saveOrder(Order order) {
            System.out.println("[MySQL DB] Замовлення " + order.getOrderId() + " на суму " + order.getTotalAmount() + " грн збережено в таблицю 'orders'.");
        }
    }

    public static class SmtpEmailService implements IEmailService {
        @Override
        public void sendOrderConfirmation(String email, String orderId) {
            System.out.println("[SMTP Email] Лист з підтвердженням замовлення " + orderId + " надіслано на " + email + ".");
        }
    }

    public static class FileAuditLogger implements IAuditLogger {
        @Override
        public void logTransaction(String message) {
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            System.out.println("[File Logger] Запис до файлу audit.log: " + time + " - " + message);
        }
    }

    // 3. Високорівневий клас бізнес-логіки, що залежить виключно від абстракцій
    public static class OrderFulfillmentService {
        private final IOrderRepository databaseRepository;
        private final IEmailService emailService;
        private final IAuditLogger fileLogger;

        // Впровадження залежностей через конструктор (Constructor Injection)
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

    // 4. Точка збірки (Composition Root) для зв'язування компонентів
    public static class CompositionRoot {
        public static OrderFulfillmentService createOrderFulfillmentService() {
            IOrderRepository databaseRepository = new MySqlDatabaseRepository();
            IEmailService emailService = new SmtpEmailService();
            IAuditLogger fileLogger = new FileAuditLogger();

            return new OrderFulfillmentService(databaseRepository, emailService, fileLogger);
        }
    }
}
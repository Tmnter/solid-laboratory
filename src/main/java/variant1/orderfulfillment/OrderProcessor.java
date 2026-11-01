package variant1.orderfulfillment;

import variant1.orderfulfillment.repository.IOrderRepository;
import variant1.orderfulfillment.repository.MySqlDatabaseRepository;
import variant1.orderfulfillment.service.IAuditLogger;
import variant1.orderfulfillment.service.FileAuditLogger;
import variant1.orderfulfillment.service.IEmailService;
import variant1.orderfulfillment.service.SmtpEmailService;
import variant1.orderfulfillment.service.OrderFulfillmentService;

public class OrderProcessor {
    public static OrderFulfillmentService createOrderFulfillmentService() {
        IOrderRepository databaseRepository = new MySqlDatabaseRepository();
        IEmailService emailService = new SmtpEmailService();
        IAuditLogger fileLogger = new FileAuditLogger();

        return new OrderFulfillmentService(databaseRepository, emailService, fileLogger);
    }
}
package main.java.variant1.orderfulfillment;

import com.example.order.repository.IOrderRepository;
import com.example.order.repository.MySqlDatabaseRepository;
import com.example.order.service.IAuditLogger;
import com.example.order.service.FileAuditLogger;
import com.example.order.service.IEmailService;
import com.example.order.service.SmtpEmailService;
import com.example.order.service.OrderFulfillmentService;

public class OrderProcessor {
    public static OrderFulfillmentService createOrderFulfillmentService() {
        IOrderRepository databaseRepository = new MySqlDatabaseRepository();
        IEmailService emailService = new SmtpEmailService();
        IAuditLogger fileLogger = new FileAuditLogger();

        return new OrderFulfillmentService(databaseRepository, emailService, fileLogger);
    }
}
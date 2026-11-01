package variant1.order.service;

import variant1.order.model.OrderCalculationResult;
import variant1.order.model.OrderItem;

import java.util.List;

/** Абстракція формування рахунку (формат може бути HTML, PDF тощо). */
public interface InvoiceGenerator {
    String generate(String customerEmail, String deliveryAddress,
                    List<OrderItem> items, OrderCalculationResult result);
}

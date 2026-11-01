package variant1.order.service;

import variant1.order.model.OrderItem;

import java.math.BigDecimal;
import java.util.List;

/** Відповідає лише за перевірку коректності вхідних даних замовлення. */
public class OrderValidator {

    public void validate(String customerEmail, List<OrderItem> items) {
        if (customerEmail == null || customerEmail.isBlank() || !customerEmail.contains("@")) {
            throw new IllegalArgumentException("Некоректна електронна адреса покупця.");
        }

        if (items == null || items.isEmpty()) {
            throw new IllegalStateException("Кошик порожній.");
        }

        for (OrderItem item : items) {
            if (item.getQuantity() <= 0
                    || item.getPrice() == null
                    || item.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException(
                        "Некоректний товар " + item.getName() + ": ціна та кількість мають бути більше 0.");
            }
        }
    }
}

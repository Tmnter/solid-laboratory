package variant1.order.model;

import java.math.BigDecimal;

/**
 * Позиція замовлення. Незмінний (immutable) об'єкт: стан задається лише в конструкторі.
 * Бізнес-валідація (ціна/кількість > 0) виконується в OrderValidator.
 */
public class OrderItem {
    private final String itemId;
    private final String name;
    private final BigDecimal price;
    private final int quantity;

    public OrderItem(String itemId, String name, BigDecimal price, int quantity) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getItemId() { return itemId; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public int getQuantity() { return quantity; }
}

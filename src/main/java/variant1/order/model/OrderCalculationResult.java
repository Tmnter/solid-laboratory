package variant1.order.model;

import java.math.BigDecimal;

/**
 * Результат розрахунку. Усі суми вже округлені до 2 знаків,
 * тому правило округлення живе лише в OrderCalculator.
 */
public class OrderCalculationResult {
    private final CustomerCategory category;
    private final BigDecimal subtotal;
    private final BigDecimal discountAmount;
    private final BigDecimal taxAmount;
    private final BigDecimal totalAmount;

    public OrderCalculationResult(CustomerCategory category, BigDecimal subtotal,
                                  BigDecimal discountAmount, BigDecimal taxAmount, BigDecimal totalAmount) {
        this.category = category;
        this.subtotal = subtotal;
        this.discountAmount = discountAmount;
        this.taxAmount = taxAmount;
        this.totalAmount = totalAmount;
    }

    public CustomerCategory getCategory() { return category; }
    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public BigDecimal getTaxAmount() { return taxAmount; }
    public BigDecimal getTotalAmount() { return totalAmount; }
}

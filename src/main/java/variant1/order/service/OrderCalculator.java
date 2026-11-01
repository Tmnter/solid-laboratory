package variant1.order.service;

import variant1.order.model.CustomerCategory;
import variant1.order.model.OrderCalculationResult;
import variant1.order.model.OrderItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/** Відповідає лише за розрахунок вартості: проміжна сума, знижка, ПДВ, підсумок. */
public class OrderCalculator {
    private static final BigDecimal VAT_RATE = new BigDecimal("0.20");
    private static final int SCALE = 2;

    public OrderCalculationResult calculate(List<OrderItem> items, CustomerCategory category) {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (OrderItem item : items) {
            subtotal = subtotal.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        subtotal = round(subtotal);

        BigDecimal discountAmount = round(subtotal.multiply(category.getDiscountRate()));
        BigDecimal taxableSubtotal = subtotal.subtract(discountAmount);
        BigDecimal taxAmount = round(taxableSubtotal.multiply(VAT_RATE));
        BigDecimal totalAmount = taxableSubtotal.add(taxAmount);

        return new OrderCalculationResult(category, subtotal, discountAmount, taxAmount, totalAmount);
    }

    private static BigDecimal round(BigDecimal value) {
        return value.setScale(SCALE, RoundingMode.HALF_UP);
    }
}

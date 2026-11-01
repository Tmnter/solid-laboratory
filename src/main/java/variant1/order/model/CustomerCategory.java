package variant1.order.model;

import java.math.BigDecimal;

/**
 * Категорія покупця. Єдине джерело правди (SSOT) для ставок знижок.
 */
public enum CustomerCategory {
    VIP(new BigDecimal("0.15")),
    REGULAR(new BigDecimal("0.05")),
    STANDARD(BigDecimal.ZERO);

    private final BigDecimal discountRate;

    CustomerCategory(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    /** Невідома або порожня категорія дає STANDARD (без знижки), як і у вихідному коді. */
    public static CustomerCategory fromString(String value) {
        if (value == null) {
            return STANDARD;
        }
        for (CustomerCategory category : values()) {
            if (category.name().equalsIgnoreCase(value.trim())) {
                return category;
            }
        }
        return STANDARD;
    }
}

package variant1.order.repository;

import java.math.BigDecimal;

/** Абстракція сховища замовлень. */
public interface OrderRepository {
    /** @throws OrderPersistenceException якщо замовлення не вдалося зберегти */
    void save(String customerEmail, BigDecimal totalAmount, String deliveryAddress);
}

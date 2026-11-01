package variant1.order.repository;

/** Помилка збереження замовлення (замість проковтування IOException). */
public class OrderPersistenceException extends RuntimeException {
    public OrderPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}

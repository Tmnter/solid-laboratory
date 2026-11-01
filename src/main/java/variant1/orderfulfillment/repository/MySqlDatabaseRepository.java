package variant1.orderfulfillment.repository;

import variant1.orderfulfillment.model.Order;

public class MySqlDatabaseRepository implements IOrderRepository {
    @Override
    public void saveOrder(Order order) {
        System.out.println("[MySQL DB] Замовлення " + order.getOrderId() + " на суму " + order.getTotalAmount() + " грн збережено в таблицю 'orders'.");
    }
}
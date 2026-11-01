package main.java.variant1.orderfulfillment.repository;

import com.example.order.model.Order;

public class MySqlDatabaseRepository implements IOrderRepository {
    @Override
    public void saveOrder(Order order) {
        System.out.println("[MySQL DB] Замовлення " + order.getOrderId() + " на суму " + order.getTotalAmount() + " грн збережено в таблицю 'orders'.");
    }
}
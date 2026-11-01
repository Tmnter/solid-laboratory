package main.java.variant1.orderfulfillment.repository;

import com.example.order.model.Order;

public interface IOrderRepository {
    void saveOrder(Order order);
}
package variant1.orderfulfillment.repository;

import variant1.orderfulfillment.model.Order;

public interface IOrderRepository {
    void saveOrder(Order order);
}
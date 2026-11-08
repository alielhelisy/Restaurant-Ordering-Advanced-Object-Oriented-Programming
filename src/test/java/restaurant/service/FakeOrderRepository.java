package restaurant.service;

import java.util.HashMap;
import java.util.Map;

import restaurant.exception.OrderNotFoundException;
import restaurant.order.Order;
import restaurant.order.OrderRepository;

/** Test stand-in for a real store. It also counts how many times an order was saved. */
final class FakeOrderRepository implements OrderRepository {

    private final Map<String, Order> orders = new HashMap<>();
    private int saveCount;

    @Override
    public void save(Order order) {
        orders.put(order.getId(), order);
        saveCount++;
    }

    @Override
    public Order findById(String orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }
        return order;
    }

    int getSaveCount() {
        return saveCount;
    }
}

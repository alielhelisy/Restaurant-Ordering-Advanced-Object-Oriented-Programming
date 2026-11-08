package restaurant.order;

import java.util.HashMap;
import java.util.Map;

import restaurant.common.Guard;
import restaurant.exception.OrderNotFoundException;

/** Keeps orders in memory. */
public final class InMemoryOrderRepository implements OrderRepository {

    private final Map<String, Order> orders = new HashMap<>();

    @Override
    public void save(Order order) {
        Guard.requireNotNull(order, "Order");
        orders.put(order.getId(), order);
    }

    @Override
    public Order findById(String orderId) {
        Guard.requireNotBlank(orderId, "Order id");
        Order order = orders.get(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }
        return order;
    }
}

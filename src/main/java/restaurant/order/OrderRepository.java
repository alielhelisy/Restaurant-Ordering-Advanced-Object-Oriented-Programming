package restaurant.order;

/** Where orders are kept. Callers do not need to know how they are stored. */
public interface OrderRepository {

    void save(Order order);

    /** Returns the order with this id, or throws OrderNotFoundException. */
    Order findById(String orderId);
}

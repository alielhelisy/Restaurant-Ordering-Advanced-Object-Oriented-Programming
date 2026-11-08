package restaurant.order;

/** The stages of an order. Only an OPEN order can change. */
public enum OrderStatus {
    OPEN,
    PAID,
    CANCELLED
}

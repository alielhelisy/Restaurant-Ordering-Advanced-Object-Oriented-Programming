package restaurant.exception;

/** Thrown when asking for the payment of an order that has not been paid. */
public final class OrderNotPaidException extends RestaurantException {

    private static final long serialVersionUID = 1L;

    public OrderNotPaidException(String orderId) {
        super("Order " + orderId + " has not been paid.");
    }
}

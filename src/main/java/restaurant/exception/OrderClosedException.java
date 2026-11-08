package restaurant.exception;

/** Thrown when an order that is already paid or cancelled is asked to change. */
public final class OrderClosedException extends RestaurantException {

    private static final long serialVersionUID = 1L;

    public OrderClosedException(String orderId, String status) {
        super("Order " + orderId + " is " + status + " and can no longer be changed.");
    }
}

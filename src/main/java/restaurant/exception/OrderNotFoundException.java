package restaurant.exception;

/** Thrown when an order id does not exist. */
public final class OrderNotFoundException extends RestaurantException {

    private static final long serialVersionUID = 1L;

    public OrderNotFoundException(String orderId) {
        super("There is no order with id '" + orderId + "'.");
    }
}

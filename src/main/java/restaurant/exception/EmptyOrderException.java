package restaurant.exception;

/** Thrown when paying an order that has no items. */
public final class EmptyOrderException extends RestaurantException {

    private static final long serialVersionUID = 1L;

    public EmptyOrderException(String orderId) {
        super("Order " + orderId + " has no items and cannot be paid.");
    }
}

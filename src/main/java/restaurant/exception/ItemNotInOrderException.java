package restaurant.exception;

/** Thrown when removing an item that the order does not contain. */
public final class ItemNotInOrderException extends RestaurantException {

    private static final long serialVersionUID = 1L;

    public ItemNotInOrderException(String orderId, String itemId) {
        super("Order " + orderId + " does not contain item '" + itemId + "'.");
    }
}

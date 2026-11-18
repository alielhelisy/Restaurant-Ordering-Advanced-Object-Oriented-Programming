package restaurant.exception;

/** Thrown when a menu item exists but has been deactivated for now. */
public final class ItemUnavailableException extends RestaurantException {

    private static final long serialVersionUID = 1L;

    public ItemUnavailableException(String itemId) {
        super("Menu item '" + itemId + "' is not available right now.");
    }
}

package restaurant.exception;

/** Thrown when a menu item is added with an id the menu already uses. */
public final class DuplicateMenuItemException extends RestaurantException {

    private static final long serialVersionUID = 1L;

    public DuplicateMenuItemException(String itemId) {
        super("The menu already has an item with id '" + itemId + "'.");
    }
}

package restaurant.exception;

/** Thrown when an item id does not exist on the menu. */
public final class MenuItemNotFoundException extends RestaurantException {

    private static final long serialVersionUID = 1L;

    public MenuItemNotFoundException(String itemId) {
        super("There is no menu item with id '" + itemId + "'.");
    }
}

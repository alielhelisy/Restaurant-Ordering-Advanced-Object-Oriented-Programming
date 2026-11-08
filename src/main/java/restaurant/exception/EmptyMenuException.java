package restaurant.exception;

/** Thrown when the menu is requested before any item has been added. */
public final class EmptyMenuException extends RestaurantException {

    private static final long serialVersionUID = 1L;

    public EmptyMenuException() {
        super("The menu has no items.");
    }
}

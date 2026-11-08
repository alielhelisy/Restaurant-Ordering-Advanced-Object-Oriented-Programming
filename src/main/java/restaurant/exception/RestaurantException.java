package restaurant.exception;

/**
 * Base type of every error this system reports.
 * It is unchecked so that adding a new failure does not change the signature of every caller.
 */
public abstract class RestaurantException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    protected RestaurantException(String message) {
        super(message);
    }
}

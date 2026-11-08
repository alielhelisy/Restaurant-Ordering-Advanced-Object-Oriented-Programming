package restaurant.exception;

/** Thrown when a value passed to an operation is missing or outside its allowed range. */
public final class InvalidInputException extends RestaurantException {

    private static final long serialVersionUID = 1L;

    public InvalidInputException(String message) {
        super(message);
    }
}

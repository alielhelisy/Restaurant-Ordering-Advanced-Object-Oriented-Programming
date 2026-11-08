package restaurant.exception;

import java.time.LocalDate;
import java.time.YearMonth;

/** Thrown when a card is used after its expiry month. */
public final class ExpiredCardException extends RestaurantException {

    private static final long serialVersionUID = 1L;

    public ExpiredCardException(YearMonth expiry, LocalDate date) {
        super("The card expired in " + expiry + " and cannot be used on " + date + ".");
    }
}

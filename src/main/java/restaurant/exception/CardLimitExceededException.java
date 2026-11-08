package restaurant.exception;

import java.math.BigDecimal;

/** Thrown when a charge is higher than the card's limit. */
public final class CardLimitExceededException extends RestaurantException {

    private static final long serialVersionUID = 1L;

    public CardLimitExceededException(BigDecimal charge, BigDecimal limit) {
        super("A charge of " + charge + " is over the card limit of " + limit + ".");
    }
}

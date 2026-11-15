package restaurant.exception;

import java.math.BigDecimal;

/** Thrown when a meal card balance is less than the amount due. */
public final class InsufficientBalanceException extends RestaurantException {

    private static final long serialVersionUID = 1L;

    public InsufficientBalanceException(BigDecimal balance, BigDecimal due) {
        super("Meal card balance " + balance + " does not cover the amount due " + due + ".");
    }
}

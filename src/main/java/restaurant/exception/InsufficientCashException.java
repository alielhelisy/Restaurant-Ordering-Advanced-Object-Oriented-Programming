package restaurant.exception;

import java.math.BigDecimal;

/** Thrown when the cash handed over is less than the amount due. */
public final class InsufficientCashException extends RestaurantException {

    private static final long serialVersionUID = 1L;

    public InsufficientCashException(BigDecimal tendered, BigDecimal due) {
        super("Cash tendered " + tendered + " does not cover the amount due " + due + ".");
    }
}

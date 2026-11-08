package restaurant.payment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import restaurant.common.Guard;
import restaurant.exception.InsufficientCashException;

/** Cash rule: the customer must hand over at least the amount due and gets the difference back. */
public final class CashPayment implements PaymentMethod {

    private static final String METHOD_NAME = "CASH";

    private final BigDecimal tendered;

    public CashPayment(BigDecimal tendered) {
        this.tendered = Guard.requirePositive(tendered, "Cash tendered").setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public Payment pay(BigDecimal amount, LocalDate date) {
        Guard.requirePositive(amount, "Payment amount");
        Guard.requireNotNull(date, "Payment date");
        if (tendered.compareTo(amount) < 0) {
            throw new InsufficientCashException(tendered, amount);
        }
        return new Payment(METHOD_NAME, amount, tendered.subtract(amount), date);
    }
}

package restaurant.payment;

import java.math.BigDecimal;
import java.time.LocalDate;

/** A way of paying. Each implementation applies its own rules (Strategy pattern). */
public interface PaymentMethod {

    /**
     * Takes payment for the given amount on the given date.
     * Throws a RestaurantException when this method cannot accept the payment.
     */
    Payment pay(BigDecimal amount, LocalDate date);
}

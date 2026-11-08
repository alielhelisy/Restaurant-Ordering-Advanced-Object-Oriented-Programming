package restaurant.payment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;

import restaurant.common.Guard;
import restaurant.exception.CardLimitExceededException;
import restaurant.exception.ExpiredCardException;

/** Card rule: a two percent fee is added, the card must not be expired and the charge must fit its limit. */
public final class CardPayment implements PaymentMethod {

    private static final String METHOD_NAME = "CARD";
    private static final BigDecimal FEE_RATE = new BigDecimal("0.02");

    private final YearMonth expiry;
    private final BigDecimal limit;

    public CardPayment(YearMonth expiry, BigDecimal limit) {
        this.expiry = Guard.requireNotNull(expiry, "Card expiry");
        this.limit = Guard.requirePositive(limit, "Card limit");
    }

    @Override
    public Payment pay(BigDecimal amount, LocalDate date) {
        Guard.requirePositive(amount, "Payment amount");
        Guard.requireNotNull(date, "Payment date");
        if (YearMonth.from(date).isAfter(expiry)) {
            throw new ExpiredCardException(expiry, date);
        }
        BigDecimal charge = amount.multiply(BigDecimal.ONE.add(FEE_RATE)).setScale(2, RoundingMode.HALF_UP);
        if (charge.compareTo(limit) > 0) {
            throw new CardLimitExceededException(charge, limit);
        }
        return new Payment(METHOD_NAME, charge, BigDecimal.ZERO.setScale(2), date);
    }
}

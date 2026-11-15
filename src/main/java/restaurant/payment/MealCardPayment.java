package restaurant.payment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import restaurant.common.Guard;
import restaurant.exception.InsufficientBalanceException;

/** Meal card rule: the card balance must cover the amount due, and the amount is taken from the balance. */
public final class MealCardPayment implements PaymentMethod {

    private static final String METHOD_NAME = "MEAL_CARD";

    private BigDecimal balance;

    public MealCardPayment(BigDecimal balance) {
        this.balance = Guard.requireNotNegative(balance, "Meal card balance").setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public Payment pay(BigDecimal amount, LocalDate date) {
        Guard.requirePositive(amount, "Payment amount");
        Guard.requireNotNull(date, "Payment date");
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException(balance, amount);
        }
        balance = balance.subtract(amount);
        return new Payment(METHOD_NAME, amount, BigDecimal.ZERO.setScale(2), date);
    }
}

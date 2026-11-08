package restaurant.payment;

import java.math.BigDecimal;
import java.time.LocalDate;

import restaurant.common.Guard;

/** The record of a payment that has been made. */
public final class Payment {

    private final String methodName;
    private final BigDecimal amountCharged;
    private final BigDecimal changeGiven;
    private final LocalDate date;

    public Payment(String methodName, BigDecimal amountCharged, BigDecimal changeGiven, LocalDate date) {
        this.methodName = Guard.requireNotBlank(methodName, "Payment method name");
        this.amountCharged = Guard.requirePositive(amountCharged, "Amount charged");
        this.changeGiven = Guard.requireNotNegative(changeGiven, "Change given");
        this.date = Guard.requireNotNull(date, "Payment date");
    }

    public String getMethodName() {
        return methodName;
    }

    public BigDecimal getAmountCharged() {
        return amountCharged;
    }

    public BigDecimal getChangeGiven() {
        return changeGiven;
    }

    public LocalDate getDate() {
        return date;
    }
}

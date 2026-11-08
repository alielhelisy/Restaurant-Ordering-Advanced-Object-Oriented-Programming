package restaurant.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import restaurant.payment.Payment;
import restaurant.payment.PaymentMethod;

/** Test stand-in for a payment method. It always accepts and remembers the amount it was asked for. */
final class RecordingPaymentMethod implements PaymentMethod {

    private BigDecimal lastAmount;

    @Override
    public Payment pay(BigDecimal amount, LocalDate date) {
        lastAmount = amount;
        return new Payment("FAKE", amount, BigDecimal.ZERO, date);
    }

    BigDecimal getLastAmount() {
        return lastAmount;
    }
}

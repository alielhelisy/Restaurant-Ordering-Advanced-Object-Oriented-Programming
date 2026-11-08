package restaurant.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import restaurant.exception.InsufficientCashException;
import restaurant.exception.InvalidInputException;

class CashPaymentTest {

    private static final BigDecimal DUE = new BigDecimal("150.00");
    private static final LocalDate DATE = LocalDate.of(2026, 11, 8);

    @Test
    void handingOverMoreThanTheAmountGivesChange() {
        // Arrange
        CashPayment cash = new CashPayment(new BigDecimal("200.00"));

        // Act
        Payment payment = cash.pay(DUE, DATE);

        // Assert
        assertEquals(new BigDecimal("50.00"), payment.getChangeGiven());
    }

    @Test
    void handingOverTheExactAmountGivesNoChange() {
        // Arrange
        CashPayment cash = new CashPayment(DUE);

        // Act
        Payment payment = cash.pay(DUE, DATE);

        // Assert
        assertEquals(new BigDecimal("0.00"), payment.getChangeGiven());
    }

    @Test
    void handingOverOneCentTooLittleThrows() {
        // Arrange
        CashPayment cash = new CashPayment(new BigDecimal("149.99"));

        // Act + Assert
        InsufficientCashException ex = assertThrows(
                InsufficientCashException.class,
                () -> cash.pay(DUE, DATE)
        );
        assertEquals("Cash tendered 149.99 does not cover the amount due 150.00.", ex.getMessage());
    }

    @Test
    void cashIsChargedWithoutAFee() {
        // Arrange
        CashPayment cash = new CashPayment(new BigDecimal("200.00"));

        // Act
        Payment payment = cash.pay(DUE, DATE);

        // Assert
        assertEquals(DUE, payment.getAmountCharged());
    }

    @Test
    void paymentIsLabelledAsCash() {
        // Arrange
        CashPayment cash = new CashPayment(DUE);

        // Act
        Payment payment = cash.pay(DUE, DATE);

        // Assert
        assertEquals("CASH", payment.getMethodName());
    }

    @Test
    void paymentKeepsTheDateItWasGiven() {
        // Arrange
        CashPayment cash = new CashPayment(DUE);

        // Act
        Payment payment = cash.pay(DUE, DATE);

        // Assert
        assertEquals(DATE, payment.getDate());
    }

    @Test
    void zeroAmountDueIsRejected() {
        // Arrange
        CashPayment cash = new CashPayment(DUE);

        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> cash.pay(BigDecimal.ZERO, DATE)
        );
        assertEquals("Payment amount must be greater than zero, but was 0.", ex.getMessage());
    }

    @Test
    void missingDateIsRejected() {
        // Arrange
        CashPayment cash = new CashPayment(DUE);

        // Act + Assert
        assertThrows(
                InvalidInputException.class,
                () -> cash.pay(DUE, null)
        );
    }

    @Test
    void zeroCashTenderedIsRejected() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> new CashPayment(BigDecimal.ZERO)
        );
        assertEquals("Cash tendered must be greater than zero, but was 0.", ex.getMessage());
    }
}

package restaurant.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import restaurant.exception.InvalidInputException;

class PaymentTest {

    private static final BigDecimal AMOUNT = new BigDecimal("100.00");
    private static final BigDecimal NO_CHANGE = new BigDecimal("0.00");
    private static final LocalDate DATE = LocalDate.of(2026, 11, 8);

    @Test
    void newPaymentKeepsItsDetails() {
        // Act
        Payment payment = new Payment("CASH", AMOUNT, NO_CHANGE, DATE);

        // Assert
        assertEquals("CASH", payment.getMethodName());
        assertEquals(AMOUNT, payment.getAmountCharged());
        assertEquals(NO_CHANGE, payment.getChangeGiven());
        assertEquals(DATE, payment.getDate());
    }

    @Test
    void blankMethodNameIsRejected() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> new Payment(" ", AMOUNT, NO_CHANGE, DATE)
        );
        assertEquals("Payment method name must not be blank.", ex.getMessage());
    }

    @Test
    void zeroAmountChargedIsRejected() {
        // Act + Assert
        assertThrows(
                InvalidInputException.class,
                () -> new Payment("CASH", BigDecimal.ZERO, NO_CHANGE, DATE)
        );
    }

    @Test
    void negativeChangeIsRejected() {
        // Arrange
        BigDecimal negativeChange = new BigDecimal("-1.00");

        // Act + Assert
        assertThrows(
                InvalidInputException.class,
                () -> new Payment("CASH", AMOUNT, negativeChange, DATE)
        );
    }

    @Test
    void missingDateIsRejected() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> new Payment("CASH", AMOUNT, NO_CHANGE, null)
        );
        assertEquals("Payment date must not be null.", ex.getMessage());
    }
}

package restaurant.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.jupiter.api.Test;

import restaurant.exception.CardLimitExceededException;
import restaurant.exception.ExpiredCardException;
import restaurant.exception.InvalidInputException;

class CardPaymentTest {

    private static final BigDecimal AMOUNT = new BigDecimal("100.00");
    private static final BigDecimal HIGH_LIMIT = new BigDecimal("1000.00");
    private static final YearMonth EXPIRY = YearMonth.of(2026, 11);
    private static final LocalDate DATE = LocalDate.of(2026, 11, 8);

    private final CardPayment card = new CardPayment(EXPIRY, HIGH_LIMIT);

    @Test
    void cardIsChargedTheAmountPlusTwoPercent() {
        // Act
        Payment payment = card.pay(AMOUNT, DATE);

        // Assert
        assertEquals(new BigDecimal("102.00"), payment.getAmountCharged());
    }

    @Test
    void feeIsRoundedToTwoDecimals() {
        // Act
        Payment payment = card.pay(new BigDecimal("10.25"), DATE);

        // Assert
        assertEquals(new BigDecimal("10.46"), payment.getAmountCharged());
    }

    @Test
    void cardPaymentGivesNoChange() {
        // Act
        Payment payment = card.pay(AMOUNT, DATE);

        // Assert
        assertEquals(new BigDecimal("0.00"), payment.getChangeGiven());
    }

    @Test
    void paymentIsLabelledAsCard() {
        // Act
        Payment payment = card.pay(AMOUNT, DATE);

        // Assert
        assertEquals("CARD", payment.getMethodName());
    }

    @Test
    void cardWorksOnTheLastDayOfItsExpiryMonth() {
        // Arrange
        LocalDate lastDay = LocalDate.of(2026, 11, 30);

        // Act
        Payment payment = card.pay(AMOUNT, lastDay);

        // Assert
        assertEquals(lastDay, payment.getDate());
    }

    @Test
    void cardIsRejectedOnTheFirstDayAfterItsExpiryMonth() {
        // Arrange
        LocalDate dayAfter = LocalDate.of(2026, 12, 1);

        // Act + Assert
        ExpiredCardException ex = assertThrows(
                ExpiredCardException.class,
                () -> card.pay(AMOUNT, dayAfter)
        );
        assertEquals("The card expired in 2026-11 and cannot be used on 2026-12-01.", ex.getMessage());
    }

    @Test
    void chargeEqualToTheLimitIsAccepted() {
        // Arrange
        CardPayment exactLimit = new CardPayment(EXPIRY, new BigDecimal("102.00"));

        // Act
        Payment payment = exactLimit.pay(AMOUNT, DATE);

        // Assert
        assertEquals(new BigDecimal("102.00"), payment.getAmountCharged());
    }

    @Test
    void chargeOneCentOverTheLimitThrows() {
        // Arrange
        CardPayment lowLimit = new CardPayment(EXPIRY, new BigDecimal("101.99"));

        // Act + Assert
        CardLimitExceededException ex = assertThrows(
                CardLimitExceededException.class,
                () -> lowLimit.pay(AMOUNT, DATE)
        );
        assertEquals("A charge of 102.00 is over the card limit of 101.99.", ex.getMessage());
    }

    @Test
    void zeroAmountIsRejected() {
        // Act + Assert
        assertThrows(
                InvalidInputException.class,
                () -> card.pay(BigDecimal.ZERO, DATE)
        );
    }

    @Test
    void missingDateIsRejected() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> card.pay(AMOUNT, null)
        );
        assertEquals("Payment date must not be null.", ex.getMessage());
    }

    @Test
    void missingExpiryIsRejected() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> new CardPayment(null, HIGH_LIMIT)
        );
        assertEquals("Card expiry must not be null.", ex.getMessage());
    }

    @Test
    void zeroLimitIsRejected() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> new CardPayment(EXPIRY, BigDecimal.ZERO)
        );
        assertEquals("Card limit must be greater than zero, but was 0.", ex.getMessage());
    }
}

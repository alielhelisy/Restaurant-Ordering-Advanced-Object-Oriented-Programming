package restaurant.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import restaurant.exception.InsufficientBalanceException;
import restaurant.exception.InvalidInputException;

class MealCardPaymentTest {

    private static final BigDecimal DUE = new BigDecimal("150.00");
    private static final LocalDate DATE = LocalDate.of(2026, 11, 15);

    @Test
    void newCardKeepsItsBalanceWithTwoDecimals() {
        // Act
        MealCardPayment card = new MealCardPayment(new BigDecimal("200"));

        // Assert
        assertEquals(new BigDecimal("200.00"), card.getBalance());
    }

    @Test
    void payingTakesTheAmountFromTheBalance() {
        // Arrange
        MealCardPayment card = new MealCardPayment(new BigDecimal("200.00"));

        // Act
        card.pay(DUE, DATE);

        // Assert
        assertEquals(new BigDecimal("50.00"), card.getBalance());
    }

    @Test
    void twoPaymentsAreBothTakenFromTheBalance() {
        // Arrange
        MealCardPayment card = new MealCardPayment(new BigDecimal("200.00"));

        // Act
        card.pay(new BigDecimal("50.00"), DATE);
        card.pay(new BigDecimal("100.00"), DATE);

        // Assert
        assertEquals(new BigDecimal("50.00"), card.getBalance());
    }

    @Test
    void payingTheWholeBalanceLeavesZero() {
        // Arrange
        MealCardPayment card = new MealCardPayment(DUE);

        // Act
        card.pay(DUE, DATE);

        // Assert
        assertEquals(new BigDecimal("0.00"), card.getBalance());
    }

    @Test
    void payingOneCentMoreThanTheBalanceThrows() {
        // Arrange
        MealCardPayment card = new MealCardPayment(new BigDecimal("149.99"));

        // Act + Assert
        InsufficientBalanceException ex = assertThrows(
                InsufficientBalanceException.class,
                () -> card.pay(DUE, DATE)
        );
        assertEquals("Meal card balance 149.99 does not cover the amount due 150.00.", ex.getMessage());
    }

    @Test
    void refusedPaymentLeavesTheBalanceUnchanged() {
        // Arrange
        MealCardPayment card = new MealCardPayment(new BigDecimal("100.00"));

        // Act
        assertThrows(
                InsufficientBalanceException.class,
                () -> card.pay(DUE, DATE)
        );

        // Assert
        assertEquals(new BigDecimal("100.00"), card.getBalance());
    }

    @Test
    void mealCardIsChargedWithoutAFee() {
        // Arrange
        MealCardPayment card = new MealCardPayment(new BigDecimal("200.00"));

        // Act
        Payment payment = card.pay(DUE, DATE);

        // Assert
        assertEquals(DUE, payment.getAmountCharged());
    }

    @Test
    void mealCardGivesNoChange() {
        // Arrange
        MealCardPayment card = new MealCardPayment(new BigDecimal("200.00"));

        // Act
        Payment payment = card.pay(DUE, DATE);

        // Assert
        assertEquals(new BigDecimal("0.00"), payment.getChangeGiven());
    }

    @Test
    void paymentIsLabelledAsMealCard() {
        // Arrange
        MealCardPayment card = new MealCardPayment(new BigDecimal("200.00"));

        // Act
        Payment payment = card.pay(DUE, DATE);

        // Assert
        assertEquals("MEAL_CARD", payment.getMethodName());
    }

    @Test
    void zeroAmountIsRejected() {
        // Arrange
        MealCardPayment card = new MealCardPayment(new BigDecimal("200.00"));

        // Act + Assert
        assertThrows(
                InvalidInputException.class,
                () -> card.pay(BigDecimal.ZERO, DATE)
        );
    }

    @Test
    void missingDateIsRejected() {
        // Arrange
        MealCardPayment card = new MealCardPayment(new BigDecimal("200.00"));

        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> card.pay(DUE, null)
        );
        assertEquals("Payment date must not be null.", ex.getMessage());
    }

    @Test
    void negativeBalanceIsRejected() {
        // Arrange
        BigDecimal negative = new BigDecimal("-1.00");

        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> new MealCardPayment(negative)
        );
        assertEquals("Meal card balance must not be negative, but was -1.00.", ex.getMessage());
    }
}

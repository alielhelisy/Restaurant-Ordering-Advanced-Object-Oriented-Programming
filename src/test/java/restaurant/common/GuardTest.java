package restaurant.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import restaurant.exception.InvalidInputException;
import restaurant.exception.RestaurantException;

class GuardTest {

    @Test
    void requireNotNullReturnsThePresentValue() {
        // Arrange
        String value = "Burger";

        // Act
        String result = Guard.requireNotNull(value, "Name");

        // Assert
        assertEquals(value, result);
    }

    @Test
    void requireNotNullRejectsNull() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> Guard.requireNotNull(null, "Name")
        );
        assertEquals("Name must not be null.", ex.getMessage());
    }

    @Test
    void invalidInputCanBeCaughtAsTheBaseException() {
        // Act + Assert
        assertThrows(
                RestaurantException.class,
                () -> Guard.requireNotNull(null, "Name")
        );
    }

    @Test
    void requireNotBlankReturnsTextWithContent() {
        // Act
        String result = Guard.requireNotBlank("Burger", "Name");

        // Assert
        assertEquals("Burger", result);
    }

    @Test
    void requireNotBlankRejectsNull() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> Guard.requireNotBlank(null, "Name")
        );
        assertEquals("Name must not be blank.", ex.getMessage());
    }

    @Test
    void requireNotBlankRejectsOnlySpaces() {
        // Act + Assert
        assertThrows(
                InvalidInputException.class,
                () -> Guard.requireNotBlank("   ", "Name")
        );
    }

    @Test
    void requirePositiveDecimalAcceptsTheSmallestAmountAboveZero() {
        // Arrange
        BigDecimal value = new BigDecimal("0.01");

        // Act
        BigDecimal result = Guard.requirePositive(value, "Price");

        // Assert
        assertEquals(value, result);
    }

    @Test
    void requirePositiveDecimalRejectsZero() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> Guard.requirePositive(BigDecimal.ZERO, "Price")
        );
        assertEquals("Price must be greater than zero, but was 0.", ex.getMessage());
    }

    @Test
    void requirePositiveDecimalRejectsNull() {
        // Act + Assert
        assertThrows(
                InvalidInputException.class,
                () -> Guard.requirePositive((BigDecimal) null, "Price")
        );
    }

    @Test
    void requireNotNegativeAcceptsZero() {
        // Act
        BigDecimal result = Guard.requireNotNegative(BigDecimal.ZERO, "Rate");

        // Assert
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void requireNotNegativeRejectsAmountBelowZero() {
        // Arrange
        BigDecimal value = new BigDecimal("-0.01");

        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> Guard.requireNotNegative(value, "Rate")
        );
        assertEquals("Rate must not be negative, but was -0.01.", ex.getMessage());
    }

    @Test
    void requireNotNegativeRejectsNull() {
        // Act + Assert
        assertThrows(
                InvalidInputException.class,
                () -> Guard.requireNotNegative(null, "Rate")
        );
    }

    @Test
    void requirePositiveIntAcceptsOne() {
        // Act
        int result = Guard.requirePositive(1, "Quantity");

        // Assert
        assertEquals(1, result);
    }

    @Test
    void requirePositiveIntRejectsZero() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> Guard.requirePositive(0, "Quantity")
        );
        assertEquals("Quantity must be greater than zero, but was 0.", ex.getMessage());
    }
}

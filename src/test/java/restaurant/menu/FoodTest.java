package restaurant.menu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import restaurant.exception.InvalidInputException;

class FoodTest {

    private static final BigDecimal PRICE = new BigDecimal("150.00");

    @Test
    void newFoodKeepsItsDetails() {
        // Act
        Food food = new Food("F1", "Burger", PRICE, 15);

        // Assert
        assertEquals("F1", food.getId());
        assertEquals("Burger", food.getName());
        assertEquals(PRICE, food.getPrice());
        assertEquals(15, food.getPreparationMinutes());
    }

    @Test
    void priceIsStoredWithTwoDecimals() {
        // Act
        Food food = new Food("F1", "Burger", new BigDecimal("150"), 15);

        // Assert
        assertEquals(new BigDecimal("150.00"), food.getPrice());
    }

    @Test
    void priceOfOneCentIsAccepted() {
        // Act
        Food food = new Food("F1", "Burger", new BigDecimal("0.01"), 15);

        // Assert
        assertEquals(new BigDecimal("0.01"), food.getPrice());
    }

    @Test
    void priceThatRoundsToZeroIsRejected() {
        // Arrange
        BigDecimal tooSmall = new BigDecimal("0.001");

        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> new Food("F1", "Burger", tooSmall, 15)
        );
        assertEquals("Menu item price must be greater than zero, but was 0.00.", ex.getMessage());
    }

    @Test
    void negativePriceIsRejected() {
        // Arrange
        BigDecimal negative = new BigDecimal("-1.00");

        // Act + Assert
        assertThrows(
                InvalidInputException.class,
                () -> new Food("F1", "Burger", negative, 15)
        );
    }

    @Test
    void missingPriceIsRejected() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> new Food("F1", "Burger", null, 15)
        );
        assertEquals("Menu item price must not be null.", ex.getMessage());
    }

    @Test
    void blankIdIsRejected() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> new Food(" ", "Burger", PRICE, 15)
        );
        assertEquals("Menu item id must not be blank.", ex.getMessage());
    }

    @Test
    void blankNameIsRejected() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> new Food("F1", " ", PRICE, 15)
        );
        assertEquals("Menu item name must not be blank.", ex.getMessage());
    }

    @Test
    void preparationTimeOfOneMinuteIsAccepted() {
        // Act
        Food food = new Food("F1", "Burger", PRICE, 1);

        // Assert
        assertEquals(1, food.getPreparationMinutes());
    }

    @Test
    void preparationTimeOfZeroIsRejected() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> new Food("F1", "Burger", PRICE, 0)
        );
        assertEquals("Preparation time must be greater than zero, but was 0.", ex.getMessage());
    }

    @Test
    void foodIsTaxedAtTenPercent() {
        // Arrange
        Food food = new Food("F1", "Burger", PRICE, 15);

        // Act
        BigDecimal rate = food.getTaxRate();

        // Assert
        assertEquals(new BigDecimal("0.10"), rate);
    }
}

package restaurant.menu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import restaurant.exception.InvalidInputException;

class DessertTest {

    private static final BigDecimal PRICE = new BigDecimal("80.00");

    @Test
    void newDessertKeepsItsCalories() {
        // Act
        Dessert dessert = new Dessert("S1", "Baklava", PRICE, 450);

        // Assert
        assertEquals(450, dessert.getCalories());
    }

    @Test
    void oneCalorieIsAccepted() {
        // Act
        Dessert dessert = new Dessert("S1", "Baklava", PRICE, 1);

        // Assert
        assertEquals(1, dessert.getCalories());
    }

    @Test
    void zeroCaloriesIsRejected() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> new Dessert("S1", "Baklava", PRICE, 0)
        );
        assertEquals("Calories must be greater than zero, but was 0.", ex.getMessage());
    }

    @Test
    void dessertIsTaxedAtFivePercent() {
        // Arrange
        Dessert dessert = new Dessert("S1", "Baklava", PRICE, 450);

        // Act
        BigDecimal rate = dessert.getTaxRate();

        // Assert
        assertEquals(new BigDecimal("0.05"), rate);
    }
}

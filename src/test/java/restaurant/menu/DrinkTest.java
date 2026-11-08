package restaurant.menu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import restaurant.exception.InvalidInputException;

class DrinkTest {

    private static final BigDecimal PRICE = new BigDecimal("40.00");

    @Test
    void newDrinkKeepsItsVolume() {
        // Act
        Drink drink = new Drink("D1", "Cola", PRICE, 330);

        // Assert
        assertEquals(330, drink.getVolumeMl());
    }

    @Test
    void volumeOfOneMillilitreIsAccepted() {
        // Act
        Drink drink = new Drink("D1", "Cola", PRICE, 1);

        // Assert
        assertEquals(1, drink.getVolumeMl());
    }

    @Test
    void volumeOfZeroIsRejected() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> new Drink("D1", "Cola", PRICE, 0)
        );
        assertEquals("Drink volume must be greater than zero, but was 0.", ex.getMessage());
    }

    @Test
    void drinkIsTaxedAtTwentyPercent() {
        // Arrange
        Drink drink = new Drink("D1", "Cola", PRICE, 330);

        // Act
        BigDecimal rate = drink.getTaxRate();

        // Assert
        assertEquals(new BigDecimal("0.20"), rate);
    }
}

package restaurant.order;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import restaurant.menu.Food;
import restaurant.menu.MenuItem;

class OrderLineTest {

    private final MenuItem burger = new Food("F1", "Burger", new BigDecimal("150.00"), 15);

    @Test
    void lineTotalMultipliesPriceByQuantity() {
        // Arrange
        OrderLine line = new OrderLine(burger, 3);

        // Act
        BigDecimal total = line.getLineTotal();

        // Assert
        assertEquals(new BigDecimal("450.00"), total);
    }

    @Test
    void increasingQuantityAddsToTheCurrentQuantity() {
        // Arrange
        OrderLine line = new OrderLine(burger, 2);

        // Act
        line.increaseQuantity(3);

        // Assert
        assertEquals(5, line.getQuantity());
    }
}

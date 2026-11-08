package restaurant.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import restaurant.customer.Customer;
import restaurant.exception.InvalidInputException;
import restaurant.menu.Drink;
import restaurant.menu.Food;
import restaurant.order.Order;

class SubtotalCalculatorTest {

    private final PriceCalculator calculator = new SubtotalCalculator();
    private final Order order = new Order("ORD-1", new Customer("C1", "Ayse"), LocalDate.of(2026, 11, 8));

    @Test
    void subtotalAddsEveryLineTotal() {
        // Arrange
        order.addItem(new Food("F1", "Burger", new BigDecimal("150.00"), 15), 2);
        order.addItem(new Drink("D1", "Cola", new BigDecimal("40.00"), 330), 1);

        // Act
        BigDecimal subtotal = calculator.calculate(order);

        // Assert
        assertEquals(new BigDecimal("340.00"), subtotal);
    }

    @Test
    void emptyOrderHasAZeroSubtotal() {
        // Act
        BigDecimal subtotal = calculator.calculate(order);

        // Assert
        assertEquals(new BigDecimal("0.00"), subtotal);
    }

    @Test
    void missingOrderIsRejected() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> calculator.calculate(null)
        );
        assertEquals("Order must not be null.", ex.getMessage());
    }
}

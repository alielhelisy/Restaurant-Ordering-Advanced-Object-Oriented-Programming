package restaurant.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import restaurant.customer.Customer;
import restaurant.exception.InvalidInputException;
import restaurant.menu.Dessert;
import restaurant.menu.Drink;
import restaurant.menu.Food;
import restaurant.order.Order;

class TaxDecoratorTest {

    private final PriceCalculator calculator = new TaxDecorator(new SubtotalCalculator());
    private final Order order = new Order("ORD-1", new Customer("C1", "Ayse"), LocalDate.of(2026, 11, 8));

    @Test
    void eachItemIsTaxedAtItsOwnRate() {
        // Arrange
        order.addItem(new Food("F1", "Burger", new BigDecimal("100.00"), 15), 1);
        order.addItem(new Drink("D1", "Cola", new BigDecimal("50.00"), 330), 1);
        order.addItem(new Dessert("S1", "Baklava", new BigDecimal("20.00"), 450), 1);

        // Act
        BigDecimal total = calculator.calculate(order);

        // Assert
        assertEquals(new BigDecimal("191.00"), total);
    }

    @Test
    void taxUsesTheQuantityOfTheLine() {
        // Arrange
        order.addItem(new Food("F1", "Burger", new BigDecimal("100.00"), 15), 3);

        // Act
        BigDecimal total = calculator.calculate(order);

        // Assert
        assertEquals(new BigDecimal("330.00"), total);
    }

    @Test
    void taxIsAddedToWhateverTheWrappedCalculatorReturns() {
        // Arrange
        PriceCalculator fixedAmount = anyOrder -> new BigDecimal("500.00");
        PriceCalculator taxed = new TaxDecorator(fixedAmount);
        order.addItem(new Food("F1", "Burger", new BigDecimal("100.00"), 15), 1);

        // Act
        BigDecimal total = taxed.calculate(order);

        // Assert
        assertEquals(new BigDecimal("510.00"), total);
    }

    @Test
    void totalIsRoundedToTwoDecimals() {
        // Arrange
        order.addItem(new Food("F1", "Burger", new BigDecimal("10.05"), 15), 1);

        // Act
        BigDecimal total = calculator.calculate(order);

        // Assert
        assertEquals(new BigDecimal("11.06"), total);
    }

    @Test
    void emptyOrderHasNoTax() {
        // Act
        BigDecimal total = calculator.calculate(order);

        // Assert
        assertEquals(new BigDecimal("0.00"), total);
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

    @Test
    void missingWrappedCalculatorIsRejected() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> new TaxDecorator(null)
        );
        assertEquals("Wrapped calculator must not be null.", ex.getMessage());
    }
}

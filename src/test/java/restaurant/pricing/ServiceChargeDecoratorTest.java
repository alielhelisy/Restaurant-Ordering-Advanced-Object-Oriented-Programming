package restaurant.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import restaurant.customer.Customer;
import restaurant.exception.InvalidInputException;
import restaurant.menu.Food;
import restaurant.order.Order;

class ServiceChargeDecoratorTest {

    private static final BigDecimal TEN_PERCENT = new BigDecimal("0.10");

    private final PriceCalculator fixedAmount = anyOrder -> new BigDecimal("100.00");
    private final Order order = new Order("ORD-1", new Customer("C1", "Ayse"), LocalDate.of(2026, 11, 8));

    @Test
    void serviceChargeIsAddedToTheWrappedAmount() {
        // Arrange
        PriceCalculator calculator = new ServiceChargeDecorator(fixedAmount, TEN_PERCENT);

        // Act
        BigDecimal total = calculator.calculate(order);

        // Assert
        assertEquals(new BigDecimal("110.00"), total);
    }

    @Test
    void zeroRateLeavesTheAmountUnchanged() {
        // Arrange
        PriceCalculator calculator = new ServiceChargeDecorator(fixedAmount, BigDecimal.ZERO);

        // Act
        BigDecimal total = calculator.calculate(order);

        // Assert
        assertEquals(new BigDecimal("100.00"), total);
    }

    @Test
    void negativeRateIsRejected() {
        // Arrange
        BigDecimal negative = new BigDecimal("-0.01");

        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> new ServiceChargeDecorator(fixedAmount, negative)
        );
        assertEquals("Service charge rate must not be negative, but was -0.01.", ex.getMessage());
    }

    @Test
    void missingRateIsRejected() {
        // Act + Assert
        assertThrows(
                InvalidInputException.class,
                () -> new ServiceChargeDecorator(fixedAmount, null)
        );
    }

    @Test
    void missingWrappedCalculatorIsRejected() {
        // Act + Assert
        assertThrows(
                InvalidInputException.class,
                () -> new ServiceChargeDecorator(null, TEN_PERCENT)
        );
    }

    @Test
    void missingOrderIsRejected() {
        // Arrange
        PriceCalculator calculator = new ServiceChargeDecorator(fixedAmount, TEN_PERCENT);

        // Act + Assert
        assertThrows(
                InvalidInputException.class,
                () -> calculator.calculate(null)
        );
    }

    @Test
    void chargeIsWorkedOutAfterTaxWhenWrappedAroundTax() {
        // Arrange
        PriceCalculator calculator =
                new ServiceChargeDecorator(new TaxDecorator(new SubtotalCalculator()), TEN_PERCENT);
        order.addItem(new Food("F1", "Burger", new BigDecimal("100.00"), 15), 1);

        // Act
        BigDecimal total = calculator.calculate(order);

        // Assert
        assertEquals(new BigDecimal("121.00"), total);
    }
}

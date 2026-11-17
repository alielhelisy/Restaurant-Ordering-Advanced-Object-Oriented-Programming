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

class DiscountDecoratorTest {

    private static final BigDecimal TEN_PERCENT = new BigDecimal("0.10");

    private final PriceCalculator fixedAmount = anyOrder -> new BigDecimal("100.00");
    private final Order order = new Order("ORD-1", new Customer("C1", "Ayse"), LocalDate.of(2026, 11, 17));

    @Test
    void discountIsTakenOffTheWrappedAmount() {
        // Arrange
        PriceCalculator calculator = new DiscountDecorator(fixedAmount, TEN_PERCENT);

        // Act
        BigDecimal total = calculator.calculate(order);

        // Assert
        assertEquals(new BigDecimal("90.00"), total);
    }

    @Test
    void zeroRateLeavesTheAmountUnchanged() {
        // Arrange
        PriceCalculator calculator = new DiscountDecorator(fixedAmount, BigDecimal.ZERO);

        // Act
        BigDecimal total = calculator.calculate(order);

        // Assert
        assertEquals(new BigDecimal("100.00"), total);
    }

    @Test
    void rateJustBelowOneIsAccepted() {
        // Arrange
        PriceCalculator calculator = new DiscountDecorator(fixedAmount, new BigDecimal("0.99"));

        // Act
        BigDecimal total = calculator.calculate(order);

        // Assert
        assertEquals(new BigDecimal("1.00"), total);
    }

    @Test
    void rateOfOneIsRejected() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> new DiscountDecorator(fixedAmount, BigDecimal.ONE)
        );
        assertEquals("Discount rate must be less than 1, but was 1.", ex.getMessage());
    }

    @Test
    void negativeRateIsRejected() {
        // Arrange
        BigDecimal negative = new BigDecimal("-0.01");

        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> new DiscountDecorator(fixedAmount, negative)
        );
        assertEquals("Discount rate must not be negative, but was -0.01.", ex.getMessage());
    }

    @Test
    void missingRateIsRejected() {
        // Act + Assert
        assertThrows(
                InvalidInputException.class,
                () -> new DiscountDecorator(fixedAmount, null)
        );
    }

    @Test
    void missingWrappedCalculatorIsRejected() {
        // Act + Assert
        assertThrows(
                InvalidInputException.class,
                () -> new DiscountDecorator(null, TEN_PERCENT)
        );
    }

    @Test
    void missingOrderIsRejected() {
        // Arrange
        PriceCalculator calculator = new DiscountDecorator(fixedAmount, TEN_PERCENT);

        // Act + Assert
        assertThrows(
                InvalidInputException.class,
                () -> calculator.calculate(null)
        );
    }

    @Test
    void discountedTotalIsRoundedToTwoDecimals() {
        // Arrange
        PriceCalculator thirtyThree = anyOrder -> new BigDecimal("33.33");
        PriceCalculator calculator = new DiscountDecorator(thirtyThree, new BigDecimal("0.15"));

        // Act
        BigDecimal total = calculator.calculate(order);

        // Assert
        assertEquals(new BigDecimal("28.33"), total);
    }

    @Test
    void discountWrappedAroundTaxAndServiceChargeIsTakenLast() {
        // Arrange
        PriceCalculator calculator = new DiscountDecorator(
                new ServiceChargeDecorator(new TaxDecorator(new SubtotalCalculator()), TEN_PERCENT),
                new BigDecimal("0.20"));
        order.addItem(new Food("F1", "Burger", new BigDecimal("100.00"), 15), 1);

        // Act
        BigDecimal total = calculator.calculate(order);

        // Assert
        assertEquals(new BigDecimal("96.80"), total);
    }
}

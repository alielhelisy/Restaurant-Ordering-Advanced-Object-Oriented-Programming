package restaurant.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;

import restaurant.common.Guard;
import restaurant.order.Order;

/**
 * Wraps another calculator and adjusts its result (Decorator pattern).
 * Charges can be combined freely by wrapping one inside another.
 */
public abstract class PriceCalculatorDecorator implements PriceCalculator {

    private final PriceCalculator wrapped;

    protected PriceCalculatorDecorator(PriceCalculator wrapped) {
        this.wrapped = Guard.requireNotNull(wrapped, "Wrapped calculator");
    }

    @Override
    public final BigDecimal calculate(Order order) {
        Guard.requireNotNull(order, "Order");
        return adjust(wrapped.calculate(order), order).setScale(2, RoundingMode.HALF_UP);
    }

    protected abstract BigDecimal adjust(BigDecimal amountSoFar, Order order);
}

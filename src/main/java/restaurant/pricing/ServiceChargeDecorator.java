package restaurant.pricing;

import java.math.BigDecimal;

import restaurant.common.Guard;
import restaurant.order.Order;

/** Adds a service charge as a percentage of the amount calculated so far. */
public final class ServiceChargeDecorator extends PriceCalculatorDecorator {

    private final BigDecimal rate;

    public ServiceChargeDecorator(PriceCalculator wrapped, BigDecimal rate) {
        super(wrapped);
        this.rate = Guard.requireNotNegative(rate, "Service charge rate");
    }

    @Override
    protected BigDecimal adjust(BigDecimal amountSoFar, Order order) {
        return amountSoFar.multiply(BigDecimal.ONE.add(rate));
    }
}

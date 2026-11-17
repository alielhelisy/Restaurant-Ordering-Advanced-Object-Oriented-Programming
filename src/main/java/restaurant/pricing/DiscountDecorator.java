package restaurant.pricing;

import java.math.BigDecimal;

import restaurant.common.Guard;
import restaurant.exception.InvalidInputException;
import restaurant.order.Order;

/** Takes a percentage off the amount calculated so far, for example a student discount. */
public final class DiscountDecorator extends PriceCalculatorDecorator {

    private final BigDecimal rate;

    public DiscountDecorator(PriceCalculator wrapped, BigDecimal rate) {
        super(wrapped);
        Guard.requireNotNegative(rate, "Discount rate");
        if (rate.compareTo(BigDecimal.ONE) >= 0) {
            throw new InvalidInputException("Discount rate must be less than 1, but was " + rate + ".");
        }
        this.rate = rate;
    }

    @Override
    protected BigDecimal adjust(BigDecimal amountSoFar, Order order) {
        return amountSoFar.multiply(BigDecimal.ONE.subtract(rate));
    }
}

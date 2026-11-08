package restaurant.pricing;

import java.math.BigDecimal;

import restaurant.order.Order;
import restaurant.order.OrderLine;

/** Adds the tax of every line, using the rate of that line's own item. */
public final class TaxDecorator extends PriceCalculatorDecorator {

    public TaxDecorator(PriceCalculator wrapped) {
        super(wrapped);
    }

    @Override
    protected BigDecimal adjust(BigDecimal amountSoFar, Order order) {
        BigDecimal withTax = amountSoFar;
        for (OrderLine line : order.getLines()) {
            withTax = withTax.add(line.getLineTotal().multiply(line.getItem().getTaxRate()));
        }
        return withTax;
    }
}

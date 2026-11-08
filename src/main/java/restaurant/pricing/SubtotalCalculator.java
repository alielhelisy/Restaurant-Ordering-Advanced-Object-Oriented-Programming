package restaurant.pricing;

import java.math.BigDecimal;

import restaurant.common.Guard;
import restaurant.order.Order;
import restaurant.order.OrderLine;

/** The plain price of an order: the sum of its lines, before any tax or charge. */
public final class SubtotalCalculator implements PriceCalculator {

    @Override
    public BigDecimal calculate(Order order) {
        Guard.requireNotNull(order, "Order");
        BigDecimal subtotal = BigDecimal.ZERO.setScale(2);
        for (OrderLine line : order.getLines()) {
            subtotal = subtotal.add(line.getLineTotal());
        }
        return subtotal;
    }
}

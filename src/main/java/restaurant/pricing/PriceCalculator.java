package restaurant.pricing;

import java.math.BigDecimal;

import restaurant.order.Order;

/** Works out how much an order costs. */
public interface PriceCalculator {

    BigDecimal calculate(Order order);
}

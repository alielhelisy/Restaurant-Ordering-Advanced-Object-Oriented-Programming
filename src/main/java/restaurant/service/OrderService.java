package restaurant.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import restaurant.common.Guard;
import restaurant.customer.Customer;
import restaurant.menu.Menu;
import restaurant.menu.MenuItem;
import restaurant.order.Order;
import restaurant.order.OrderRepository;
import restaurant.payment.Payment;
import restaurant.payment.PaymentMethod;
import restaurant.pricing.PriceCalculator;

/**
 * The operations of the restaurant. It only coordinates the other classes.
 * Its collaborators are passed in, so tests can replace them with fakes.
 */
public final class OrderService {

    private static final String ORDER_ID_PREFIX = "ORD-";

    private final Menu menu;
    private final OrderRepository orders;
    private final PriceCalculator priceCalculator;
    private int nextOrderNumber = 1;

    public OrderService(Menu menu, OrderRepository orders, PriceCalculator priceCalculator) {
        this.menu = Guard.requireNotNull(menu, "Menu");
        this.orders = Guard.requireNotNull(orders, "Order repository");
        this.priceCalculator = Guard.requireNotNull(priceCalculator, "Price calculator");
    }

    public List<MenuItem> getMenu() {
        return menu.getAvailableItems();
    }

    public Order createOrder(Customer customer, LocalDate date) {
        Order order = new Order(ORDER_ID_PREFIX + nextOrderNumber, customer, date);
        nextOrderNumber++;
        orders.save(order);
        return order;
    }

    public void addItem(String orderId, String itemId, int quantity) {
        Order order = orders.findById(orderId);
        order.addItem(menu.findAvailableById(itemId), quantity);
        orders.save(order);
    }

    public void removeItem(String orderId, String itemId) {
        Order order = orders.findById(orderId);
        order.removeItem(itemId);
        orders.save(order);
    }

    public BigDecimal getOrderTotal(String orderId) {
        return priceCalculator.calculate(orders.findById(orderId));
    }

    public Payment payOrder(String orderId, PaymentMethod method, LocalDate date) {
        Order order = orders.findById(orderId);
        Payment payment = order.pay(method, priceCalculator.calculate(order), date);
        orders.save(order);
        return payment;
    }

    public void cancelOrder(String orderId) {
        Order order = orders.findById(orderId);
        order.cancel();
        orders.save(order);
    }
}

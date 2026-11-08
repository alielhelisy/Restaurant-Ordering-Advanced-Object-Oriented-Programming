package restaurant.order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import restaurant.common.Guard;
import restaurant.customer.Customer;
import restaurant.exception.ItemNotInOrderException;
import restaurant.exception.OrderClosedException;
import restaurant.menu.MenuItem;

/** A customer's order. It owns its lines and enforces which changes are allowed at each status. */
public final class Order {

    private final String id;
    private final Customer customer;
    private final LocalDate date;
    private final Map<String, OrderLine> lines = new LinkedHashMap<>();
    private OrderStatus status = OrderStatus.OPEN;

    public Order(String id, Customer customer, LocalDate date) {
        this.id = Guard.requireNotBlank(id, "Order id");
        this.customer = Guard.requireNotNull(customer, "Customer");
        this.date = Guard.requireNotNull(date, "Order date");
    }

    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDate getDate() {
        return date;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<OrderLine> getLines() {
        return new ArrayList<>(lines.values());
    }

    public void addItem(MenuItem item, int quantity) {
        requireOpen();
        Guard.requireNotNull(item, "Menu item");
        Guard.requirePositive(quantity, "Quantity");
        OrderLine existing = lines.get(item.getId());
        if (existing == null) {
            lines.put(item.getId(), new OrderLine(item, quantity));
        } else {
            existing.increaseQuantity(quantity);
        }
    }

    public void removeItem(String itemId) {
        requireOpen();
        Guard.requireNotBlank(itemId, "Menu item id");
        if (lines.remove(itemId) == null) {
            throw new ItemNotInOrderException(id, itemId);
        }
    }

    public void cancel() {
        requireOpen();
        status = OrderStatus.CANCELLED;
    }

    private void requireOpen() {
        if (status != OrderStatus.OPEN) {
            throw new OrderClosedException(id, status.name());
        }
    }
}

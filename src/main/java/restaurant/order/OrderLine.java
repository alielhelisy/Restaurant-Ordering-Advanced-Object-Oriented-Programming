package restaurant.order;

import java.math.BigDecimal;

import restaurant.menu.MenuItem;

/** One menu item and how many of it were ordered. A line is created and owned by its order. */
public final class OrderLine {

    private final MenuItem item;
    private int quantity;

    OrderLine(MenuItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public MenuItem getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getLineTotal() {
        return item.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    void increaseQuantity(int extra) {
        quantity += extra;
    }

    @Override
    public String toString() {
        return quantity + " x " + item.getName();
    }
}

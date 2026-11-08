package restaurant.menu;

import java.math.BigDecimal;
import java.math.RoundingMode;

import restaurant.common.Guard;

/** Something a customer can order. Each kind of item decides its own tax rate. */
public abstract class MenuItem {

    private final String id;
    private final String name;
    private final BigDecimal price;

    protected MenuItem(String id, String name, BigDecimal price) {
        this.id = Guard.requireNotBlank(id, "Menu item id");
        this.name = Guard.requireNotBlank(name, "Menu item name");
        Guard.requireNotNull(price, "Menu item price");
        this.price = Guard.requirePositive(price.setScale(2, RoundingMode.HALF_UP), "Menu item price");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    /** Returns the tax rate of this kind of item, for example 0.10 for ten percent. */
    public abstract BigDecimal getTaxRate();
}

package restaurant.menu;

import java.math.BigDecimal;

import restaurant.common.Guard;

/** A drink served in a fixed volume. */
public final class Drink extends MenuItem {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.20");

    private final int volumeMl;

    public Drink(String id, String name, BigDecimal price, int volumeMl) {
        super(id, name, price);
        this.volumeMl = Guard.requirePositive(volumeMl, "Drink volume");
    }

    public int getVolumeMl() {
        return volumeMl;
    }

    @Override
    public BigDecimal getTaxRate() {
        return TAX_RATE;
    }
}

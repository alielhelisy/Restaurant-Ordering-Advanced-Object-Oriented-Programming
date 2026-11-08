package restaurant.menu;

import java.math.BigDecimal;

import restaurant.common.Guard;

/** A dish cooked in the kitchen. */
public final class Food extends MenuItem {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.10");

    private final int preparationMinutes;

    public Food(String id, String name, BigDecimal price, int preparationMinutes) {
        super(id, name, price);
        this.preparationMinutes = Guard.requirePositive(preparationMinutes, "Preparation time");
    }

    public int getPreparationMinutes() {
        return preparationMinutes;
    }

    @Override
    public BigDecimal getTaxRate() {
        return TAX_RATE;
    }
}

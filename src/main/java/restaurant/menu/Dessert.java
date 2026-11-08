package restaurant.menu;

import java.math.BigDecimal;

import restaurant.common.Guard;

/** A sweet dish with a known calorie count. */
public final class Dessert extends MenuItem {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.05");

    private final int calories;

    public Dessert(String id, String name, BigDecimal price, int calories) {
        super(id, name, price);
        this.calories = Guard.requirePositive(calories, "Calories");
    }

    public int getCalories() {
        return calories;
    }

    @Override
    public BigDecimal getTaxRate() {
        return TAX_RATE;
    }
}

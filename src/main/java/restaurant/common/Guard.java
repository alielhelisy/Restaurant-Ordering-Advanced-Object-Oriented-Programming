package restaurant.common;

import java.math.BigDecimal;

import restaurant.exception.InvalidInputException;

/** Shared argument checks, so that every class reports bad input the same way. */
public final class Guard {

    private Guard() {
    }

    public static <T> T requireNotNull(T value, String name) {
        if (value == null) {
            throw new InvalidInputException(name + " must not be null.");
        }
        return value;
    }

    public static String requireNotBlank(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new InvalidInputException(name + " must not be blank.");
        }
        return value;
    }

    public static BigDecimal requirePositive(BigDecimal value, String name) {
        requireNotNull(value, name);
        if (value.signum() <= 0) {
            throw new InvalidInputException(name + " must be greater than zero, but was " + value + ".");
        }
        return value;
    }

    public static BigDecimal requireNotNegative(BigDecimal value, String name) {
        requireNotNull(value, name);
        if (value.signum() < 0) {
            throw new InvalidInputException(name + " must not be negative, but was " + value + ".");
        }
        return value;
    }

    public static int requirePositive(int value, String name) {
        if (value <= 0) {
            throw new InvalidInputException(name + " must be greater than zero, but was " + value + ".");
        }
        return value;
    }
}

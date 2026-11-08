package restaurant.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import restaurant.exception.InvalidInputException;

class CustomerTest {

    @Test
    void newCustomerKeepsIdAndName() {
        // Act
        Customer customer = new Customer("C1", "Ayse");

        // Assert
        assertEquals("C1", customer.getId());
        assertEquals("Ayse", customer.getName());
    }

    @Test
    void blankIdIsRejected() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> new Customer(" ", "Ayse")
        );
        assertEquals("Customer id must not be blank.", ex.getMessage());
    }

    @Test
    void blankNameIsRejected() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> new Customer("C1", " ")
        );
        assertEquals("Customer name must not be blank.", ex.getMessage());
    }
}

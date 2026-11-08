package restaurant.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import restaurant.customer.Customer;
import restaurant.exception.InvalidInputException;
import restaurant.exception.OrderNotFoundException;

class InMemoryOrderRepositoryTest {

    private static final LocalDate DATE = LocalDate.of(2026, 11, 8);

    private final OrderRepository repository = new InMemoryOrderRepository();
    private final Customer customer = new Customer("C1", "Ayse");
    private final Order order = new Order("ORD-1", customer, DATE);

    @Test
    void savedOrderCanBeFoundById() {
        // Arrange
        repository.save(order);

        // Act
        Order found = repository.findById("ORD-1");

        // Assert
        assertSame(order, found);
    }

    @Test
    void savingAnOrderWithTheSameIdReplacesTheOldOne() {
        // Arrange
        Order replacement = new Order("ORD-1", customer, DATE.plusDays(1));
        repository.save(order);

        // Act
        repository.save(replacement);

        // Assert
        assertSame(replacement, repository.findById("ORD-1"));
    }

    @Test
    void savingNoOrderIsRejected() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> repository.save(null)
        );
        assertEquals("Order must not be null.", ex.getMessage());
    }

    @Test
    void findingAnUnknownOrderThrows() {
        // Act + Assert
        OrderNotFoundException ex = assertThrows(
                OrderNotFoundException.class,
                () -> repository.findById("ORD-9")
        );
        assertEquals("There is no order with id 'ORD-9'.", ex.getMessage());
    }

    @Test
    void findingWithABlankIdIsRejected() {
        // Act + Assert
        assertThrows(
                InvalidInputException.class,
                () -> repository.findById(" ")
        );
    }
}

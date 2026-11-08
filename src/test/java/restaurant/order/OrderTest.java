package restaurant.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import restaurant.customer.Customer;
import restaurant.exception.InvalidInputException;
import restaurant.exception.ItemNotInOrderException;
import restaurant.exception.OrderClosedException;
import restaurant.menu.Drink;
import restaurant.menu.Food;
import restaurant.menu.MenuItem;

class OrderTest {

    private static final LocalDate DATE = LocalDate.of(2026, 11, 8);

    private final Customer customer = new Customer("C1", "Ayse");
    private final MenuItem burger = new Food("F1", "Burger", new BigDecimal("150.00"), 15);
    private final MenuItem cola = new Drink("D1", "Cola", new BigDecimal("40.00"), 330);
    private final Order order = new Order("ORD-1", customer, DATE);

    @Test
    void newOrderIsOpen() {
        // Act
        OrderStatus status = order.getStatus();

        // Assert
        assertEquals(OrderStatus.OPEN, status);
    }

    @Test
    void newOrderKeepsItsDetails() {
        // Assert
        assertEquals("ORD-1", order.getId());
        assertSame(customer, order.getCustomer());
        assertEquals(DATE, order.getDate());
    }

    @Test
    void newOrderHasNoLines() {
        // Act
        List<OrderLine> lines = order.getLines();

        // Assert
        assertTrue(lines.isEmpty());
    }

    @Test
    void blankIdIsRejected() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> new Order(" ", customer, DATE)
        );
        assertEquals("Order id must not be blank.", ex.getMessage());
    }

    @Test
    void missingCustomerIsRejected() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> new Order("ORD-1", null, DATE)
        );
        assertEquals("Customer must not be null.", ex.getMessage());
    }

    @Test
    void missingDateIsRejected() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> new Order("ORD-1", customer, null)
        );
        assertEquals("Order date must not be null.", ex.getMessage());
    }

    @Test
    void addingAnItemCreatesALineWithThatQuantity() {
        // Act
        order.addItem(burger, 2);

        // Assert
        OrderLine line = order.getLines().get(0);
        assertSame(burger, line.getItem());
        assertEquals(2, line.getQuantity());
    }

    @Test
    void addingTheSameItemAgainIncreasesItsQuantity() {
        // Arrange
        order.addItem(burger, 2);

        // Act
        order.addItem(burger, 3);

        // Assert
        assertEquals(1, order.getLines().size());
        assertEquals(5, order.getLines().get(0).getQuantity());
    }

    @Test
    void addingDifferentItemsKeepsSeparateLines() {
        // Act
        order.addItem(burger, 1);
        order.addItem(cola, 1);

        // Assert
        assertEquals(2, order.getLines().size());
    }

    @Test
    void quantityOfOneIsAccepted() {
        // Act
        order.addItem(burger, 1);

        // Assert
        assertEquals(1, order.getLines().get(0).getQuantity());
    }

    @Test
    void quantityOfZeroIsRejected() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> order.addItem(burger, 0)
        );
        assertEquals("Quantity must be greater than zero, but was 0.", ex.getMessage());
    }

    @Test
    void addingNoItemIsRejected() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> order.addItem(null, 1)
        );
        assertEquals("Menu item must not be null.", ex.getMessage());
    }

    @Test
    void addingToACancelledOrderThrows() {
        // Arrange
        order.cancel();

        // Act + Assert
        OrderClosedException ex = assertThrows(
                OrderClosedException.class,
                () -> order.addItem(burger, 1)
        );
        assertEquals("Order ORD-1 is CANCELLED and can no longer be changed.", ex.getMessage());
    }

    @Test
    void removingAnItemTakesItsLineOut() {
        // Arrange
        order.addItem(burger, 2);
        order.addItem(cola, 1);

        // Act
        order.removeItem("F1");

        // Assert
        assertEquals(1, order.getLines().size());
        assertSame(cola, order.getLines().get(0).getItem());
    }

    @Test
    void removingAnItemNotInTheOrderThrows() {
        // Arrange
        order.addItem(burger, 1);

        // Act + Assert
        ItemNotInOrderException ex = assertThrows(
                ItemNotInOrderException.class,
                () -> order.removeItem("D1")
        );
        assertEquals("Order ORD-1 does not contain item 'D1'.", ex.getMessage());
    }

    @Test
    void removingWithABlankIdIsRejected() {
        // Act + Assert
        assertThrows(
                InvalidInputException.class,
                () -> order.removeItem(" ")
        );
    }

    @Test
    void removingFromACancelledOrderThrows() {
        // Arrange
        order.addItem(burger, 1);
        order.cancel();

        // Act + Assert
        assertThrows(
                OrderClosedException.class,
                () -> order.removeItem("F1")
        );
    }

    @Test
    void changingTheReturnedLinesDoesNotChangeTheOrder() {
        // Arrange
        order.addItem(burger, 1);
        List<OrderLine> lines = order.getLines();

        // Act
        lines.clear();

        // Assert
        assertEquals(1, order.getLines().size());
    }

    @Test
    void cancellingMarksTheOrderAsCancelled() {
        // Act
        order.cancel();

        // Assert
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void cancellingTwiceThrows() {
        // Arrange
        order.cancel();

        // Act + Assert
        assertThrows(
                OrderClosedException.class,
                order::cancel
        );
    }
}

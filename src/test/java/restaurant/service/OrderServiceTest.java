package restaurant.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import restaurant.customer.Customer;
import restaurant.exception.EmptyMenuException;
import restaurant.exception.EmptyOrderException;
import restaurant.exception.InsufficientCashException;
import restaurant.exception.InvalidInputException;
import restaurant.exception.ItemNotInOrderException;
import restaurant.exception.ItemUnavailableException;
import restaurant.exception.MenuItemNotFoundException;
import restaurant.exception.OrderClosedException;
import restaurant.exception.OrderNotFoundException;
import restaurant.menu.Drink;
import restaurant.menu.Food;
import restaurant.menu.Menu;
import restaurant.menu.MenuItem;
import restaurant.order.Order;
import restaurant.order.OrderStatus;
import restaurant.payment.Payment;
import restaurant.payment.PaymentMethod;
import restaurant.pricing.PriceCalculator;

class OrderServiceTest {

    private static final LocalDate DATE = LocalDate.of(2026, 11, 8);
    private static final BigDecimal FIXED_TOTAL = new BigDecimal("123.45");

    private final MenuItem burger = new Food("F1", "Burger", new BigDecimal("150.00"), 15);
    private final Customer customer = new Customer("C1", "Ayse");
    private final Menu menu = new Menu();
    private final FakeOrderRepository repository = new FakeOrderRepository();
    private final PriceCalculator fixedPrice = order -> FIXED_TOTAL;
    private final RecordingPaymentMethod paymentMethod = new RecordingPaymentMethod();
    private OrderService service;

    @BeforeEach
    void setUp() {
        menu.addItem(burger);
        service = new OrderService(menu, repository, fixedPrice);
    }

    private Order orderWithBurger() {
        Order order = service.createOrder(customer, DATE);
        service.addItem(order.getId(), "F1", 1);
        return order;
    }

    @Test
    void missingMenuIsRejected() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> new OrderService(null, repository, fixedPrice)
        );
        assertEquals("Menu must not be null.", ex.getMessage());
    }

    @Test
    void missingRepositoryIsRejected() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> new OrderService(menu, null, fixedPrice)
        );
        assertEquals("Order repository must not be null.", ex.getMessage());
    }

    @Test
    void missingPriceCalculatorIsRejected() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> new OrderService(menu, repository, null)
        );
        assertEquals("Price calculator must not be null.", ex.getMessage());
    }

    @Test
    void getMenuReturnsTheItemsOnTheMenu() {
        // Act
        List<MenuItem> items = service.getMenu();

        // Assert
        assertEquals(List.of(burger), items);
    }

    @Test
    void getMenuThrowsWhenTheMenuIsEmpty() {
        // Arrange
        OrderService emptyRestaurant = new OrderService(new Menu(), repository, fixedPrice);

        // Act + Assert
        assertThrows(
                EmptyMenuException.class,
                emptyRestaurant::getMenu
        );
    }

    @Test
    void createdOrderStartsOpenForThatCustomer() {
        // Act
        Order order = service.createOrder(customer, DATE);

        // Assert
        assertEquals(OrderStatus.OPEN, order.getStatus());
        assertSame(customer, order.getCustomer());
    }

    @Test
    void createdOrderIsSaved() {
        // Act
        Order order = service.createOrder(customer, DATE);

        // Assert
        assertSame(order, repository.findById(order.getId()));
    }

    @Test
    void eachCreatedOrderGetsTheNextId() {
        // Act
        Order first = service.createOrder(customer, DATE);
        Order second = service.createOrder(customer, DATE);

        // Assert
        assertEquals("ORD-1", first.getId());
        assertEquals("ORD-2", second.getId());
    }

    @Test
    void creatingAnOrderWithoutACustomerIsRejected() {
        // Act + Assert
        assertThrows(
                InvalidInputException.class,
                () -> service.createOrder(null, DATE)
        );
    }

    @Test
    void creatingAnOrderWithoutADateIsRejected() {
        // Act + Assert
        assertThrows(
                InvalidInputException.class,
                () -> service.createOrder(customer, null)
        );
    }

    @Test
    void addItemPutsTheMenuItemOnTheOrder() {
        // Arrange
        Order order = service.createOrder(customer, DATE);

        // Act
        service.addItem(order.getId(), "F1", 2);

        // Assert
        assertSame(burger, order.getLines().get(0).getItem());
        assertEquals(2, order.getLines().get(0).getQuantity());
    }

    @Test
    void addItemSavesTheChangedOrder() {
        // Arrange
        Order order = service.createOrder(customer, DATE);
        int savesBefore = repository.getSaveCount();

        // Act
        service.addItem(order.getId(), "F1", 1);

        // Assert
        assertEquals(savesBefore + 1, repository.getSaveCount());
    }

    @Test
    void addItemThrowsForAnUnknownOrder() {
        // Act + Assert
        OrderNotFoundException ex = assertThrows(
                OrderNotFoundException.class,
                () -> service.addItem("ORD-9", "F1", 1)
        );
        assertEquals("There is no order with id 'ORD-9'.", ex.getMessage());
    }

    @Test
    void addItemThrowsForAnItemNotOnTheMenu() {
        // Arrange
        Order order = service.createOrder(customer, DATE);

        // Act + Assert
        assertThrows(
                MenuItemNotFoundException.class,
                () -> service.addItem(order.getId(), "X9", 1)
        );
    }

    @Test
    void addItemThrowsForADeactivatedItem() {
        // Arrange
        Order order = service.createOrder(customer, DATE);
        menu.deactivate("F1");

        // Act + Assert
        assertThrows(
                ItemUnavailableException.class,
                () -> service.addItem(order.getId(), "F1", 1)
        );
    }

    @Test
    void getMenuLeavesOutDeactivatedItems() {
        // Arrange
        MenuItem cola = new Drink("D1", "Cola", new BigDecimal("40.00"), 330);
        menu.addItem(cola);
        menu.deactivate("F1");

        // Act
        List<MenuItem> items = service.getMenu();

        // Assert
        assertEquals(List.of(cola), items);
    }

    @Test
    void addItemRejectsAQuantityOfZero() {
        // Arrange
        Order order = service.createOrder(customer, DATE);

        // Act + Assert
        assertThrows(
                InvalidInputException.class,
                () -> service.addItem(order.getId(), "F1", 0)
        );
    }

    @Test
    void removeItemTakesTheItemOffTheOrder() {
        // Arrange
        Order order = orderWithBurger();

        // Act
        service.removeItem(order.getId(), "F1");

        // Assert
        assertTrue(order.getLines().isEmpty());
    }

    @Test
    void removeItemSavesTheChangedOrder() {
        // Arrange
        Order order = orderWithBurger();
        int savesBefore = repository.getSaveCount();

        // Act
        service.removeItem(order.getId(), "F1");

        // Assert
        assertEquals(savesBefore + 1, repository.getSaveCount());
    }

    @Test
    void removeItemThrowsForAnUnknownOrder() {
        // Act + Assert
        assertThrows(
                OrderNotFoundException.class,
                () -> service.removeItem("ORD-9", "F1")
        );
    }

    @Test
    void removeItemThrowsWhenTheItemIsNotOnTheOrder() {
        // Arrange
        Order order = service.createOrder(customer, DATE);

        // Act + Assert
        assertThrows(
                ItemNotInOrderException.class,
                () -> service.removeItem(order.getId(), "F1")
        );
    }

    @Test
    void orderTotalComesFromThePriceCalculator() {
        // Arrange
        Order order = orderWithBurger();

        // Act
        BigDecimal total = service.getOrderTotal(order.getId());

        // Assert
        assertEquals(FIXED_TOTAL, total);
    }

    @Test
    void orderTotalThrowsForAnUnknownOrder() {
        // Act + Assert
        assertThrows(
                OrderNotFoundException.class,
                () -> service.getOrderTotal("ORD-9")
        );
    }

    @Test
    void payOrderChargesTheCalculatedTotal() {
        // Arrange
        Order order = orderWithBurger();

        // Act
        service.payOrder(order.getId(), paymentMethod, DATE);

        // Assert
        assertEquals(FIXED_TOTAL, paymentMethod.getLastAmount());
    }

    @Test
    void payOrderReturnsThePaymentStoredOnTheOrder() {
        // Arrange
        Order order = orderWithBurger();

        // Act
        Payment payment = service.payOrder(order.getId(), paymentMethod, DATE);

        // Assert
        assertSame(order.getPayment(), payment);
    }

    @Test
    void payOrderMarksTheOrderAsPaid() {
        // Arrange
        Order order = orderWithBurger();

        // Act
        service.payOrder(order.getId(), paymentMethod, DATE);

        // Assert
        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void payOrderSavesThePaidOrder() {
        // Arrange
        Order order = orderWithBurger();
        int savesBefore = repository.getSaveCount();

        // Act
        service.payOrder(order.getId(), paymentMethod, DATE);

        // Assert
        assertEquals(savesBefore + 1, repository.getSaveCount());
    }

    @Test
    void payOrderThrowsForAnUnknownOrder() {
        // Act + Assert
        assertThrows(
                OrderNotFoundException.class,
                () -> service.payOrder("ORD-9", paymentMethod, DATE)
        );
    }

    @Test
    void payOrderThrowsForAnEmptyOrder() {
        // Arrange
        Order order = service.createOrder(customer, DATE);

        // Act + Assert
        assertThrows(
                EmptyOrderException.class,
                () -> service.payOrder(order.getId(), paymentMethod, DATE)
        );
    }

    @Test
    void payOrderRejectsAMissingPaymentMethod() {
        // Arrange
        Order order = orderWithBurger();

        // Act + Assert
        assertThrows(
                InvalidInputException.class,
                () -> service.payOrder(order.getId(), null, DATE)
        );
    }

    @Test
    void refusedPaymentLeavesTheOrderOpen() {
        // Arrange
        Order order = orderWithBurger();
        PaymentMethod refusing = (amount, date) -> {
            throw new InsufficientCashException(BigDecimal.ONE, amount);
        };

        // Act
        assertThrows(
                InsufficientCashException.class,
                () -> service.payOrder(order.getId(), refusing, DATE)
        );

        // Assert
        assertEquals(OrderStatus.OPEN, order.getStatus());
    }

    @Test
    void cancelOrderMarksTheOrderAsCancelled() {
        // Arrange
        Order order = orderWithBurger();

        // Act
        service.cancelOrder(order.getId());

        // Assert
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void cancelOrderSavesTheCancelledOrder() {
        // Arrange
        Order order = orderWithBurger();
        int savesBefore = repository.getSaveCount();

        // Act
        service.cancelOrder(order.getId());

        // Assert
        assertEquals(savesBefore + 1, repository.getSaveCount());
    }

    @Test
    void cancelOrderThrowsForAnUnknownOrder() {
        // Act + Assert
        assertThrows(
                OrderNotFoundException.class,
                () -> service.cancelOrder("ORD-9")
        );
    }

    @Test
    void cancelOrderThrowsWhenTheOrderIsAlreadyPaid() {
        // Arrange
        Order order = orderWithBurger();
        service.payOrder(order.getId(), paymentMethod, DATE);

        // Act + Assert
        assertThrows(
                OrderClosedException.class,
                () -> service.cancelOrder(order.getId())
        );
    }
}

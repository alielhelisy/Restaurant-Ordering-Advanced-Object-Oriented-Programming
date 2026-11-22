package restaurant.order;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import restaurant.customer.Customer;
import restaurant.menu.Drink;
import restaurant.menu.Food;
import restaurant.payment.CashPayment;
import restaurant.payment.Payment;

/** toString() gives readable text, so failing tests show values instead of memory addresses. */
class ReadableTextTest {

    private final Customer customer = new Customer("C1", "Ayse");
    private final Food burger = new Food("F1", "Burger", new BigDecimal("150"), 15);

    @Test
    void menuItemShowsTypeIdNameAndPrice() {
        // Act
        String text = burger.toString();

        // Assert
        assertEquals("Food(F1, Burger, 150.00)", text);
    }

    @Test
    void drinkShowsItsOwnTypeName() {
        // Arrange
        Drink cola = new Drink("D1", "Cola", new BigDecimal("40.00"), 330);

        // Act
        String text = cola.toString();

        // Assert
        assertEquals("Drink(D1, Cola, 40.00)", text);
    }

    @Test
    void customerShowsIdAndName() {
        // Act
        String text = customer.toString();

        // Assert
        assertEquals("Customer(C1, Ayse)", text);
    }

    @Test
    void orderLineShowsQuantityAndItemName() {
        // Arrange
        OrderLine line = new OrderLine(burger, 2);

        // Act
        String text = line.toString();

        // Assert
        assertEquals("2 x Burger", text);
    }

    @Test
    void orderShowsIdCustomerStatusAndLines() {
        // Arrange
        Order order = new Order("ORD-1", customer, LocalDate.of(2026, 11, 22));
        order.addItem(burger, 2);

        // Act
        String text = order.toString();

        // Assert
        assertEquals("Order(ORD-1, Ayse, OPEN, [2 x Burger])", text);
    }

    @Test
    void paymentShowsMethodAmountChangeAndDate() {
        // Arrange
        Payment payment = new CashPayment(new BigDecimal("200.00"))
                .pay(new BigDecimal("150.00"), LocalDate.of(2026, 11, 22));

        // Act
        String text = payment.toString();

        // Assert
        assertEquals("Payment(CASH, 150.00, change 50.00, 2026-11-22)", text);
    }
}

package restaurant.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import restaurant.customer.Customer;
import restaurant.menu.Drink;
import restaurant.menu.Food;
import restaurant.menu.Menu;
import restaurant.order.InMemoryOrderRepository;
import restaurant.order.Order;
import restaurant.payment.CardPayment;
import restaurant.payment.CashPayment;
import restaurant.payment.Payment;
import restaurant.pricing.PriceCalculator;
import restaurant.pricing.ServiceChargeDecorator;
import restaurant.pricing.SubtotalCalculator;
import restaurant.pricing.TaxDecorator;

/** Runs a whole order through the real classes, with no fakes. */
class OrderServiceFullFlowTest {

    private static final LocalDate DATE = LocalDate.of(2026, 11, 8);

    private OrderService service;
    private Order order;

    @BeforeEach
    void setUp() {
        Menu menu = new Menu();
        menu.addItem(new Food("F1", "Burger", new BigDecimal("100.00"), 15));
        menu.addItem(new Drink("D1", "Cola", new BigDecimal("50.00"), 330));
        PriceCalculator pricing =
                new ServiceChargeDecorator(new TaxDecorator(new SubtotalCalculator()), new BigDecimal("0.10"));
        service = new OrderService(menu, new InMemoryOrderRepository(), pricing);

        order = service.createOrder(new Customer("C1", "Ayse"), DATE);
        service.addItem(order.getId(), "F1", 2);
        service.addItem(order.getId(), "D1", 1);
    }

    @Test
    void totalIncludesTaxAndServiceCharge() {
        // Act
        BigDecimal total = service.getOrderTotal(order.getId());

        // Assert
        assertEquals(new BigDecimal("308.00"), total);
    }

    @Test
    void cashCustomerPaysTheTotalAndGetsChange() {
        // Arrange
        CashPayment cash = new CashPayment(new BigDecimal("350.00"));

        // Act
        Payment payment = service.payOrder(order.getId(), cash, DATE);

        // Assert
        assertEquals(new BigDecimal("308.00"), payment.getAmountCharged());
        assertEquals(new BigDecimal("42.00"), payment.getChangeGiven());
    }

    @Test
    void cardCustomerPaysTheTotalPlusTheCardFee() {
        // Arrange
        CardPayment card = new CardPayment(YearMonth.of(2027, 1), new BigDecimal("1000.00"));

        // Act
        Payment payment = service.payOrder(order.getId(), card, DATE);

        // Assert
        assertEquals(new BigDecimal("314.16"), payment.getAmountCharged());
    }
}

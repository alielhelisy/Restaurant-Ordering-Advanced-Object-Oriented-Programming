package restaurant.menu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import restaurant.exception.DuplicateMenuItemException;
import restaurant.exception.EmptyMenuException;
import restaurant.exception.InvalidInputException;
import restaurant.exception.ItemUnavailableException;
import restaurant.exception.MenuItemNotFoundException;

class MenuTest {

    private final Menu menu = new Menu();
    private final MenuItem burger = new Food("F1", "Burger", new BigDecimal("150.00"), 15);
    private final MenuItem cola = new Drink("D1", "Cola", new BigDecimal("40.00"), 330);

    @Test
    void addedItemCanBeFoundById() {
        // Arrange
        menu.addItem(burger);

        // Act
        MenuItem found = menu.findById("F1");

        // Assert
        assertSame(burger, found);
    }

    @Test
    void addingAnItemWithAnExistingIdThrows() {
        // Arrange
        menu.addItem(burger);
        MenuItem sameId = new Dessert("F1", "Baklava", new BigDecimal("80.00"), 450);

        // Act + Assert
        DuplicateMenuItemException ex = assertThrows(
                DuplicateMenuItemException.class,
                () -> menu.addItem(sameId)
        );
        assertEquals("The menu already has an item with id 'F1'.", ex.getMessage());
    }

    @Test
    void addingNoItemThrows() {
        // Act + Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> menu.addItem(null)
        );
        assertEquals("Menu item must not be null.", ex.getMessage());
    }

    @Test
    void findingAnUnknownItemThrows() {
        // Arrange
        menu.addItem(burger);

        // Act + Assert
        MenuItemNotFoundException ex = assertThrows(
                MenuItemNotFoundException.class,
                () -> menu.findById("X9")
        );
        assertEquals("There is no menu item with id 'X9'.", ex.getMessage());
    }

    @Test
    void findingWithABlankIdThrows() {
        // Act + Assert
        assertThrows(
                InvalidInputException.class,
                () -> menu.findById(" ")
        );
    }

    @Test
    void getItemsReturnsItemsInTheOrderTheyWereAdded() {
        // Arrange
        menu.addItem(cola);
        menu.addItem(burger);

        // Act
        List<MenuItem> items = menu.getItems();

        // Assert
        assertEquals(List.of(cola, burger), items);
    }

    @Test
    void getItemsThrowsWhenTheMenuIsEmpty() {
        // Act + Assert
        EmptyMenuException ex = assertThrows(
                EmptyMenuException.class,
                menu::getItems
        );
        assertEquals("The menu has no items.", ex.getMessage());
    }

    @Test
    void newItemIsAvailable() {
        // Arrange
        menu.addItem(burger);

        // Act
        boolean available = menu.isAvailable("F1");

        // Assert
        assertTrue(available);
    }

    @Test
    void deactivatedItemIsNotAvailable() {
        // Arrange
        menu.addItem(burger);

        // Act
        menu.deactivate("F1");

        // Assert
        assertFalse(menu.isAvailable("F1"));
    }

    @Test
    void activatingMakesTheItemAvailableAgain() {
        // Arrange
        menu.addItem(burger);
        menu.deactivate("F1");

        // Act
        menu.activate("F1");

        // Assert
        assertTrue(menu.isAvailable("F1"));
    }

    @Test
    void deactivatingAnUnknownItemThrows() {
        // Act + Assert
        assertThrows(
                MenuItemNotFoundException.class,
                () -> menu.deactivate("X9")
        );
    }

    @Test
    void activatingAnUnknownItemThrows() {
        // Act + Assert
        assertThrows(
                MenuItemNotFoundException.class,
                () -> menu.activate("X9")
        );
    }

    @Test
    void askingAvailabilityOfAnUnknownItemThrows() {
        // Act + Assert
        assertThrows(
                MenuItemNotFoundException.class,
                () -> menu.isAvailable("X9")
        );
    }

    @Test
    void findAvailableByIdReturnsAnAvailableItem() {
        // Arrange
        menu.addItem(burger);

        // Act
        MenuItem found = menu.findAvailableById("F1");

        // Assert
        assertSame(burger, found);
    }

    @Test
    void findAvailableByIdThrowsForADeactivatedItem() {
        // Arrange
        menu.addItem(burger);
        menu.deactivate("F1");

        // Act + Assert
        ItemUnavailableException ex = assertThrows(
                ItemUnavailableException.class,
                () -> menu.findAvailableById("F1")
        );
        assertEquals("Menu item 'F1' is not available right now.", ex.getMessage());
    }

    @Test
    void findAvailableByIdThrowsForAnUnknownItem() {
        // Act + Assert
        assertThrows(
                MenuItemNotFoundException.class,
                () -> menu.findAvailableById("X9")
        );
    }

    @Test
    void availableItemsLeaveOutDeactivatedItems() {
        // Arrange
        menu.addItem(burger);
        menu.addItem(cola);
        menu.deactivate("F1");

        // Act
        List<MenuItem> items = menu.getAvailableItems();

        // Assert
        assertEquals(List.of(cola), items);
    }

    @Test
    void availableItemsThrowWhenEveryItemIsDeactivated() {
        // Arrange
        menu.addItem(burger);
        menu.deactivate("F1");

        // Act + Assert
        assertThrows(
                EmptyMenuException.class,
                menu::getAvailableItems
        );
    }

    @Test
    void fullMenuStillListsDeactivatedItems() {
        // Arrange
        menu.addItem(burger);
        menu.deactivate("F1");

        // Act
        List<MenuItem> items = menu.getItems();

        // Assert
        assertEquals(List.of(burger), items);
    }

    @Test
    void changingTheReturnedListDoesNotChangeTheMenu() {
        // Arrange
        menu.addItem(burger);
        List<MenuItem> items = menu.getItems();

        // Act
        items.add(cola);

        // Assert
        assertEquals(List.of(burger), menu.getItems());
    }
}

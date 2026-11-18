package restaurant.menu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import restaurant.common.Guard;
import restaurant.exception.DuplicateMenuItemException;
import restaurant.exception.EmptyMenuException;
import restaurant.exception.ItemUnavailableException;
import restaurant.exception.MenuItemNotFoundException;

/**
 * The items a restaurant offers. Items exist independently of any order.
 * An item can be deactivated for a while (for example when it is sold out) without being removed.
 */
public final class Menu {

    private final Map<String, MenuItem> items = new LinkedHashMap<>();
    private final Set<String> deactivated = new HashSet<>();

    public void addItem(MenuItem item) {
        Guard.requireNotNull(item, "Menu item");
        if (items.containsKey(item.getId())) {
            throw new DuplicateMenuItemException(item.getId());
        }
        items.put(item.getId(), item);
    }

    public MenuItem findById(String itemId) {
        Guard.requireNotBlank(itemId, "Menu item id");
        MenuItem item = items.get(itemId);
        if (item == null) {
            throw new MenuItemNotFoundException(itemId);
        }
        return item;
    }

    /** Returns the item only if it can be ordered right now. */
    public MenuItem findAvailableById(String itemId) {
        MenuItem item = findById(itemId);
        if (deactivated.contains(itemId)) {
            throw new ItemUnavailableException(itemId);
        }
        return item;
    }

    public void deactivate(String itemId) {
        findById(itemId);
        deactivated.add(itemId);
    }

    public void activate(String itemId) {
        findById(itemId);
        deactivated.remove(itemId);
    }

    public boolean isAvailable(String itemId) {
        findById(itemId);
        return !deactivated.contains(itemId);
    }

    public List<MenuItem> getItems() {
        if (items.isEmpty()) {
            throw new EmptyMenuException();
        }
        return new ArrayList<>(items.values());
    }

    /** The menu a customer sees: only the items that can be ordered now. */
    public List<MenuItem> getAvailableItems() {
        List<MenuItem> available = new ArrayList<>();
        for (MenuItem item : items.values()) {
            if (!deactivated.contains(item.getId())) {
                available.add(item);
            }
        }
        if (available.isEmpty()) {
            throw new EmptyMenuException();
        }
        return available;
    }
}

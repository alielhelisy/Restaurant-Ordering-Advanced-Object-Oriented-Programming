package restaurant.menu;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import restaurant.common.Guard;
import restaurant.exception.DuplicateMenuItemException;
import restaurant.exception.EmptyMenuException;
import restaurant.exception.MenuItemNotFoundException;

/** The items a restaurant offers. Items exist independently of any order. */
public final class Menu {

    private final Map<String, MenuItem> items = new LinkedHashMap<>();

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

    public List<MenuItem> getItems() {
        if (items.isEmpty()) {
            throw new EmptyMenuException();
        }
        return new ArrayList<>(items.values());
    }
}

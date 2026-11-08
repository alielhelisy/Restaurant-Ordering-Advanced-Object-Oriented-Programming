package restaurant.customer;

import restaurant.common.Guard;

/** A person who places orders. */
public final class Customer {

    private final String id;
    private final String name;

    public Customer(String id, String name) {
        this.id = Guard.requireNotBlank(id, "Customer id");
        this.name = Guard.requireNotBlank(name, "Customer name");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

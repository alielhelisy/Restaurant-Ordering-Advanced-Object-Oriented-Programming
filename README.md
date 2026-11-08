# Restaurant Ordering

Java classes and JUnit 5 tests for the orders of a small restaurant. There is no running program. The tests are the only way the code runs.

## Run the tests

In Eclipse, choose File > Import > Maven > Existing Maven Projects, then right-click the project and choose Run As > JUnit Test.

With Maven:

```
mvn test
```

## Packages

| Package | Contents |
| --- | --- |
| `restaurant.menu` | Menu and its items: food, drinks and desserts |
| `restaurant.customer` | The customer who places an order |
| `restaurant.order` | Orders, their lines and where they are stored |
| `restaurant.payment` | Cash and card payments (Strategy) |
| `restaurant.pricing` | Subtotal, tax and service charge (Decorator) |
| `restaurant.service` | The restaurant operations |
| `restaurant.exception` | One base exception and the specific ones |
| `restaurant.common` | Shared input checks |

## Rules used

| Rule | Value |
| --- | --- |
| Tax on food | 10% |
| Tax on drinks | 20% |
| Tax on desserts | 5% |
| Cash | The amount handed over must cover the amount due, and the difference is given back |
| Card | A 2% fee is added, the card must not be expired, and the charge must fit the card limit |

Message template sdk [![Maven Central](https://img.shields.io/maven-central/v/io.github.vladshyrokyi/template-message-sdk)](https://search.maven.org/artifact/io.github.vladshyrokyi/template-message-sdk) [![GitHub release (latest by date)](https://img.shields.io/github/v/release/VladShyrokyi/template-message-java-sdk)](https://github.com/VladShyrokyi/template-message-java-sdk)
====

# Overview

A small library for creating templates from blocks using code. The same blocks can be used for different templates. The
basic principle of working with the library is to provide a flexible and simple system for writing dynamically generated
text.

# Writing a template with variables

The writer's regex is passed a `template` with selectors, a `regex` string to find these selectors, and
a `lambda function` to create selectors.

```c#
var writer = new RegexTextWriter(
    "I hold %[ITEM]% in my %[HAND]%",
    "%\\[([^%,\\s]+)\\]%",
    s => $"%[{s}]%"
);
var text = writer.ToWriting(new Dictionary<string, string>
{
    {"ITEM", "tea"},
    {"HAND", "left hand"}
});
Assert.AreEqual("I hold tea in my left hand", text);
```

If you do not pass a value to any variable, it is replaced with a `default value`, which can also be passed (by default,
this is an empty string).

# Generating template from blocks

Using `TemplateBlockFactory` for creating blocks with `default regex`.

## Order example

```java
var deliveryConfiguration=TextBlockFactory.createTemplate(
        "Delivery method: %[METHOD]%\n"+
        "Delivery address: %[ADDRESS]%\n"+
        "Delivery date: %[DATE]%",
        Map.of(
                "METHOD",TextBlockFactory.createText("courier"),
                "ADDRESS",TextBlockFactory.createText("Khreschatyk St, 32, Kyiv, 02000"),
                "DATE",TextBlockFactory.createText("08.04.2021")
        )
);
var paymentConfiguration=TextBlockFactory.createTemplate(
        "Payment method: %[METHOD]%",
        Map.of("METHOD",TextBlockFactory.createText("cash"))
);
var orderConfiguration=TextBlockFactory.createTemplate(
        "Customer name: %[CUSTOMER_NAME]%\n"+
        "Phone: %[PHONE]%\n"+
        "Email address: %[EMAIL]%\n"+
        "%[DELIVERY_CONFIGURATION]%\n"+
        "%[PAYMENT_CONFIGURATION]%\n"+
        "Comment to order: %[COMMENT]%\n"+
        "Order date: %[ORDER_DATE]%",
        Map.of(
                "CUSTOMER_NAME",TextBlockFactory.createText("Vladislav Shirokiy"),
                "PHONE",TextBlockFactory.createText("+8888888888"),
                "EMAIL",TextBlockFactory.createText("vlad16062001@gmail.com"),
                "DELIVERY_CONFIGURATION",deliveryConfiguration,
                "PAYMENT_CONFIGURATION",paymentConfiguration,
                "COMMENT",TextBlockFactory.createText("Deliver quickly!"),
                "ORDER_DATE",TextBlockFactory.createText("04.08.2021")
        )
);

class Order {
    final Integer number;
    final String item;
    final Integer count;
    final Integer price;

    public Order(Integer number, String item, Integer count, Integer price) {
        this.number = number;
        this.item = item;
        this.count = count;
        this.price = price;
    }
}
Function<Order, TextBlockContract> orderFactory = order -> TextBlockFactory.createTemplate(
        "%[NUMBER]%. %[ITEM]% %[COUNT]%x%[PRICE]% $",
        Map.of(
                "NUMBER", TextBlockFactory.createText(order.number.toString()),
                "ITEM", TextBlockFactory.createText(order.item),
                "COUNT", TextBlockFactory.createText(order.count.toString()),
                "PRICE", TextBlockFactory.createText(order.price.toString())
        )
);
var orders = List.of(
        new Order(1, "Chair", 1, 25),
        new Order(2, "Table", 1, 50),
        new Order(3, "Wardrobe", 1, 45)
);
final int discount = 15;
final Integer shippingCost = 25;
var block = TextBlockFactory.createTemplate(
        "Order №%[ORDER_NUMBER]%\n" +
        "%[ORDER_CONFIGURATION]%\n" +
        "-------\n" +
        "%[ORDERS]%\n" +
        "\n" +
        "%[CHECK]%",
        Map.of(
                "ORDER_NUMBER", TextBlockFactory.createText("13"),
                "ORDER_CONFIGURATION", orderConfiguration,
                "ORDERS", TextBlockFactory.mergeTemplates(
                        "\n",
                        orders.stream().map(orderFactory).collect(Collectors.toList())
                ),
                "CHECK", TextBlockFactory.createTemplate(
                        "Discount: %[DISCOUNT]% $\n" +
                        "Shipping cost: %[SHIPPING_COST]% $\n" +
                        "Total amount: %[TOTAL_AMOUNT]% $",
                        Map.of(
                                "DISCOUNT", TextBlockFactory.createText(Integer.toString(discount)),
                                "SHIPPING_COST", TextBlockFactory.createText(shippingCost.toString()),
                                "TOTAL_AMOUNT", TextBlockFactory.createText(Integer.toString(
                                        orders.stream().reduce(
                                                0,
                                                (integer, order) -> integer + order.price * order.count,
                                                Integer::sum
                                        )
                                        + shippingCost - discount
                                ))
                        )
                )
        )
);
String result = "Order №13\n" +
                "Customer name: Vladislav Shirokiy\n" +
                "Phone: +8888888888\n" +
                "Email address: vlad16062001@gmail.com\n" +
                "Delivery method: courier\n" +
                "Delivery address: Khreschatyk St, 32, Kyiv, 02000\n" +
                "Delivery date: 08.04.2021\n" +
                "Payment method: cash\n" +
                "Comment to order: Deliver quickly!\n" +
                "Order date: 04.08.2021\n" +
                "-------\n" +
                "1. Chair 1x25 $\n" +
                "2. Table 1x50 $\n" +
                "3. Wardrobe 1x45 $\n" +
                "\n" +
                "Discount: 15 $\n" +
                "Shipping cost: 25 $\n" +
                "Total amount: 130 $";
Assertions.assertEquals(block.write(),result);
```

## How add package to your project?

### Maven

```xml

<dependency>
    <groupId>io.github.vladshyrokyi</groupId>
    <artifactId>template-message-sdk</artifactId>
    <version>1.2</version>
</dependency>
```

### Gradle
```groovy
implementation 'io.github.vladshyrokyi:template-message-sdk:1.2'
```

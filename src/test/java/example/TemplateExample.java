package example;

import io.github.vladshyrokyi.template_message_sdk.block.interfaces.TextBlockContract;
import io.github.vladshyrokyi.template_message_sdk.factory.TextBlockFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TemplateExample {
    @Test
    public void Order_example() {
        var deliveryConfiguration = TextBlockFactory.createTemplate(
                "Delivery method: %[METHOD]%\n" +
                "Delivery address: %[ADDRESS]%\n" +
                "Delivery date: %[DATE]%",
                Map.of(
                        "METHOD", TextBlockFactory.createText("courier"),
                        "ADDRESS", TextBlockFactory.createText("Khreschatyk St, 32, Kyiv, 02000"),
                        "DATE", TextBlockFactory.createText("08.04.2021")
                )
        );
        var paymentConfiguration = TextBlockFactory.createTemplate(
                "Payment method: %[METHOD]%",
                Map.of("METHOD", TextBlockFactory.createText("cash"))
        );
        var orderConfiguration = TextBlockFactory.createTemplate(
                "Customer name: %[CUSTOMER_NAME]%\n" +
                "Phone: %[PHONE]%\n" +
                "Email address: %[EMAIL]%\n" +
                "%[DELIVERY_CONFIGURATION]%\n" +
                "%[PAYMENT_CONFIGURATION]%\n" +
                "Comment to order: %[COMMENT]%\n" +
                "Order date: %[ORDER_DATE]%",
                Map.of(
                        "CUSTOMER_NAME", TextBlockFactory.createText("Vladislav Shirokiy"),
                        "PHONE", TextBlockFactory.createText("+8888888888"),
                        "EMAIL", TextBlockFactory.createText("vlad16062001@gmail.com"),
                        "DELIVERY_CONFIGURATION", deliveryConfiguration,
                        "PAYMENT_CONFIGURATION", paymentConfiguration,
                        "COMMENT", TextBlockFactory.createText("Deliver quickly!"),
                        "ORDER_DATE", TextBlockFactory.createText("04.08.2021")
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
        Assertions.assertEquals(block.write(), result);
    }
}

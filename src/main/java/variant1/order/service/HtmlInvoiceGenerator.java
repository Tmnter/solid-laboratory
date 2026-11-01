package variant1.order.service;

import variant1.order.model.OrderCalculationResult;
import variant1.order.model.OrderItem;

import java.util.List;

/** Формує HTML-рахунок. Не рахує суми і нічого не зберігає. */
public class HtmlInvoiceGenerator implements InvoiceGenerator {

    @Override
    public String generate(String customerEmail, String deliveryAddress,
                           List<OrderItem> items, OrderCalculationResult result) {
        StringBuilder html = new StringBuilder("<html><body>");
        html.append("<h1>Рахунок на замовлення для ").append(escape(customerEmail)).append("</h1>");
        html.append("<p>Адреса доставки: ").append(escape(deliveryAddress)).append("</p>");
        html.append("<table border='1'><tr><th>Товар</th><th>Кількість</th><th>Ціна</th></tr>");

        for (OrderItem item : items) {
            html.append("<tr><td>").append(escape(item.getName()))
                .append("</td><td>").append(item.getQuantity())
                .append("</td><td>").append(item.getPrice()).append(" грн</td></tr>");
        }

        html.append("</table>");
        html.append("<p>Проміжна сума: ").append(result.getSubtotal()).append(" грн</p>");
        html.append("<p>Знижка (").append(result.getCategory()).append("): -")
            .append(result.getDiscountAmount()).append(" грн</p>");
        html.append("<p>ПДВ (20%): ").append(result.getTaxAmount()).append(" грн</p>");
        html.append("<h3>Загалом до сплати: ").append(result.getTotalAmount()).append(" грн</h3>");
        html.append("</body></html>");
        return html.toString();
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}

package main.java.variant1.orderfulfillment.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private String orderId;
    private String customerEmail;
    private double totalAmount;
    private List<String> items = new ArrayList<>();

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public List<String> getItems() { return items; }
    public void setItems(List<String> items) { this.items = items; }
}
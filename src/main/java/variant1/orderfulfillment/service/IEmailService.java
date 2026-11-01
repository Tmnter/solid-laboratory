package variant1.orderfulfillment.service;

public interface IEmailService {
    void sendOrderConfirmation(String email, String orderId);
}
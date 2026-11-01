package main.java.variant1.orderfulfillment.service;

public class SmtpEmailService implements IEmailService {
    @Override
    public void sendOrderConfirmation(String email, String orderId) {
        System.out.println("[SMTP Email] Лист з підтвердженням замовлення " + orderId + " надіслано на " + email + ".");
    }
}
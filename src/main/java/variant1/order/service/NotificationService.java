package variant1.order.service;

/** Абстракція сповіщення покупця (email, SMS, push...). */
public interface NotificationService {
    void send(String recipient, String subject, String body);
}

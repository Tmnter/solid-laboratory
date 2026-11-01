package variant1.order.service;

import java.io.PrintStream;

/** Симулює відправку email через SMTP (вивід у потік). */
public class EmailNotificationService implements NotificationService {
    private final String smtpHost;
    private final PrintStream out;

    public EmailNotificationService(String smtpHost, PrintStream out) {
        this.smtpHost = smtpHost;
        this.out = out;
    }

    @Override
    public void send(String recipient, String subject, String body) {
        out.println("Надсилання email на " + recipient + " через " + smtpHost);
        out.println("Тема: " + subject);
        out.println("Тіло: " + body);
    }
}

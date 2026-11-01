package variant1.orderfulfillment.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileAuditLogger implements IAuditLogger {
    @Override
    public void logTransaction(String message) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("[File Logger] Запис до файлу audit.log: " + time + " - " + message);
    }
}
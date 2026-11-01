package variant1.order.repository;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Зберігає замовлення у текстовий файл у форматі: timestamp|email|total|address. */
public class FileOrderRepository implements OrderRepository {
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Path file;
    private final Clock clock;

    public FileOrderRepository(Path file, Clock clock) {
        this.file = file;
        this.clock = clock;
    }

    @Override
    public void save(String customerEmail, BigDecimal totalAmount, String deliveryAddress) {
        String timestamp = LocalDateTime.now(clock).format(TIMESTAMP_FORMAT);
        String record = String.format("%s|%s|%s|%s%n", timestamp, customerEmail, totalAmount.toPlainString(), deliveryAddress);
        try {
            Files.writeString(file, record, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new OrderPersistenceException("Помилка запису у файл бази даних: " + e.getMessage(), e);
        }
    }
}

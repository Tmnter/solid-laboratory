package variant1.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import variant1.order.model.CustomerCategory;
import variant1.order.model.OrderCalculationResult;
import variant1.order.model.OrderItem;
import variant1.order.repository.FileOrderRepository;
import variant1.order.repository.OrderPersistenceException;
import variant1.order.repository.OrderRepository;
import variant1.order.service.HtmlInvoiceGenerator;
import variant1.order.service.NotificationService;
import variant1.order.service.OrderCalculator;
import variant1.order.service.OrderValidator;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderServicesTest {

    private static List<OrderItem> items() {
        return List.of(
                new OrderItem("1", "Ноутбук", new BigDecimal("1000.00"), 1),
                new OrderItem("2", "Мишка", new BigDecimal("100.00"), 2));
    }

    // ---- Validator ----

    @Test
    void validatorRejectsBadEmail() {
        assertThrows(IllegalArgumentException.class, () -> new OrderValidator().validate("abc", items()));
        assertThrows(IllegalArgumentException.class, () -> new OrderValidator().validate(null, items()));
    }

    @Test
    void validatorRejectsEmptyCart() {
        assertThrows(IllegalStateException.class, () -> new OrderValidator().validate("a@b.c", List.of()));
    }

    @Test
    void validatorRejectsBadItem() {
        List<OrderItem> bad = List.of(new OrderItem("1", "X", new BigDecimal("10"), 0));
        assertThrows(IllegalArgumentException.class, () -> new OrderValidator().validate("a@b.c", bad));
    }

    // ---- Calculator ----

    @Test
    void calculatorVip() {
        OrderCalculationResult r = new OrderCalculator().calculate(items(), CustomerCategory.VIP);
        assertEquals(new BigDecimal("1200.00"), r.getSubtotal());
        assertEquals(new BigDecimal("180.00"), r.getDiscountAmount());
        assertEquals(new BigDecimal("204.00"), r.getTaxAmount());
        assertEquals(new BigDecimal("1224.00"), r.getTotalAmount());
    }

    @Test
    void calculatorRegularAndStandard() {
        OrderCalculator calc = new OrderCalculator();
        assertEquals(new BigDecimal("1368.00"), calc.calculate(items(), CustomerCategory.REGULAR).getTotalAmount());
        assertEquals(new BigDecimal("1440.00"), calc.calculate(items(), CustomerCategory.STANDARD).getTotalAmount());
    }

    @Test
    void unknownCategoryMeansNoDiscount() {
        assertEquals(CustomerCategory.STANDARD, CustomerCategory.fromString("Gold"));
        assertEquals(CustomerCategory.STANDARD, CustomerCategory.fromString(null));
        assertEquals(CustomerCategory.VIP, CustomerCategory.fromString("vip"));
    }

    // ---- Invoice ----

    @Test
    void invoiceContainsTotalsAndEscapesHtml() {
        OrderCalculationResult r = new OrderCalculator().calculate(items(), CustomerCategory.VIP);
        String html = new HtmlInvoiceGenerator().generate("a@b.c", "<b>addr</b>", items(), r);
        assertTrue(html.contains("Загалом до сплати: 1224.00 грн"));
        assertTrue(html.contains("&lt;b&gt;addr&lt;/b&gt;"));
    }

    // ---- Repository ----

    @Test
    void repositoryAppendsRecords(@TempDir Path dir) throws Exception {
        Path file = dir.resolve("orders.txt");
        Clock clock = Clock.fixed(Instant.parse("2026-01-02T03:04:05Z"), ZoneOffset.UTC);
        FileOrderRepository repo = new FileOrderRepository(file, clock);

        repo.save("a@b.c", new BigDecimal("12.50"), "addr1");
        repo.save("d@e.f", new BigDecimal("7.00"), "addr2");

        List<String> lines = Files.readAllLines(file);
        assertEquals(2, lines.size());
        assertEquals("2026-01-02 03:04:05|a@b.c|12.50|addr1", lines.get(0));
    }

    @Test
    void repositoryThrowsOnIoError(@TempDir Path dir) {
        FileOrderRepository repo = new FileOrderRepository(dir.resolve("no/such/dir/o.txt"), Clock.systemUTC());
        assertThrows(OrderPersistenceException.class, () -> repo.save("a@b.c", BigDecimal.ONE, "x"));
    }

    // ---- Orchestrator ----

    @Test
    void processorSavesAndNotifies() {
        List<String> saved = new ArrayList<>();
        List<String> sent = new ArrayList<>();
        OrderRepository repo = (email, total, addr) -> saved.add(email + "|" + total);
        NotificationService notifier = (to, subject, body) -> sent.add(to);

        OrderProcessor p = new OrderProcessor(new OrderValidator(), new OrderCalculator(),
                new HtmlInvoiceGenerator(), repo, notifier);
        p.processOrder("a@b.c", "VIP", items(), "addr");

        assertEquals(List.of("a@b.c|1224.00"), saved);
        assertEquals(List.of("a@b.c"), sent);
    }

    @Test
    void processorDoesNotNotifyWhenSaveFails() {
        List<String> sent = new ArrayList<>();
        OrderRepository failing = (email, total, addr) -> {
            throw new OrderPersistenceException("fail", new RuntimeException());
        };
        OrderProcessor p = new OrderProcessor(new OrderValidator(), new OrderCalculator(),
                new HtmlInvoiceGenerator(), failing, (to, s, b) -> sent.add(to));

        assertThrows(OrderPersistenceException.class, () -> p.processOrder("a@b.c", "VIP", items(), "addr"));
        assertTrue(sent.isEmpty());
    }

    @Test
    void processorSurvivesNotificationFailure() {
        List<String> saved = new ArrayList<>();
        OrderProcessor p = new OrderProcessor(new OrderValidator(), new OrderCalculator(),
                new HtmlInvoiceGenerator(), (e, t, a) -> saved.add(e),
                (to, s, b) -> { throw new IllegalStateException("smtp down"); });

        assertDoesNotThrow(() -> p.processOrder("a@b.c", "VIP", items(), "addr"));
        assertEquals(1, saved.size());
    }
}

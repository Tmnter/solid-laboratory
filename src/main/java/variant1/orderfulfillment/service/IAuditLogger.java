package main.java.variant1.orderfulfillment.service;

public interface IAuditLogger {
    void logTransaction(String message);
}
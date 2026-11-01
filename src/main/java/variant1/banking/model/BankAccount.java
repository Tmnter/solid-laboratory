package variant1.banking.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Базовий контракт, спільний для ВСІХ рахунків: номер, баланс, поповнення.
 * Зняття коштів сюди не входить, бо підтримується не кожним типом рахунку.
 *
 * Інваріанти: номер не порожній, баланс ніколи не від'ємний.
 */
public abstract class BankAccount {
    private final String accountNumber;
    private BigDecimal balance;

    protected BankAccount(String accountNumber, BigDecimal initialBalance) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account number must not be blank");
        }
        Objects.requireNonNull(initialBalance, "initialBalance");
        if (initialBalance.signum() < 0) {
            throw new IllegalArgumentException("Initial balance must not be negative");
        }
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    public String getAccountNumber() { return accountNumber; }
    public BigDecimal getBalance() { return balance; }

    public void deposit(PositiveAmount amount) {
        balance = balance.add(amount.value());
    }

    /** Списання з перевіркою інваріанта "баланс >= 0". Доступне лише підкласам, які його відкривають. */
    protected void debit(PositiveAmount amount) {
        if (amount.value().compareTo(balance) > 0) {
            throw new IllegalStateException("Insufficient funds");
        }
        balance = balance.subtract(amount.value());
    }
}

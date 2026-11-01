package variant1.banking.model;

import java.math.BigDecimal;

/**
 * Рахунок, з якого МОЖНА знімати кошти. Тип виражає контракт, потрібний клієнтам
 * на кшталт BankService: якщо метод приймає WithdrawableAccount, withdraw гарантовано працює.
 */
public abstract class WithdrawableAccount extends BankAccount {

    protected WithdrawableAccount(String accountNumber, BigDecimal initialBalance) {
        super(accountNumber, initialBalance);
    }

    /**
     * Передумова: amount > 0 (гарантує PositiveAmount).
     * @throws IllegalStateException якщо коштів недостатньо
     */
    public void withdraw(PositiveAmount amount) {
        debit(amount);
    }
}

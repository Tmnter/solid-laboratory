package variant1.banking.model;

import java.math.BigDecimal;

/** Звичайний поточний рахунок: поповнення та зняття без обмежень за терміном. */
public class CurrentAccount extends WithdrawableAccount {

    public CurrentAccount(String accountNumber, BigDecimal initialBalance) {
        super(accountNumber, initialBalance);
    }
}

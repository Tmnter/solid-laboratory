package variant1.banking.model;

import java.math.BigDecimal;

/**
 * Строковий депозит: приймає поповнення, але НЕ є WithdrawableAccount.
 * Метода withdraw тут немає взагалі, тому помилку "зняття з депозиту" компілятор
 * ловить заздалегідь, а не вона вилітає UnsupportedOperationException у рантаймі.
 */
public class FixedDepositAccount extends BankAccount {
    private final int termMonths;

    public FixedDepositAccount(String accountNumber, BigDecimal initialBalance, int termMonths) {
        super(accountNumber, initialBalance);
        if (termMonths <= 0) {
            throw new IllegalArgumentException("Term must be positive");
        }
        this.termMonths = termMonths;
    }

    public int getTermMonths() { return termMonths; }
}

package variant1.banking.service;

import variant1.banking.model.PositiveAmount;
import variant1.banking.model.WithdrawableAccount;

public class BankService {

    /** Приймає лише рахунки, з яких можна знімати: передати FixedDepositAccount не скомпілюється. */
    public void processMonthlyFee(WithdrawableAccount account, PositiveAmount fee) {
        account.withdraw(fee);
        System.out.println("Fee processed for " + account.getAccountNumber()
                + ". New balance: " + account.getBalance());
    }
}

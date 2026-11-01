package variant1.banking;

import org.junit.jupiter.api.Test;
import variant1.banking.model.BankAccount;
import variant1.banking.model.CurrentAccount;
import variant1.banking.model.FixedDepositAccount;
import variant1.banking.model.PositiveAmount;
import variant1.banking.model.WithdrawableAccount;
import variant1.banking.service.BankService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BankingLspTest {

    private static BigDecimal bd(String v) { return new BigDecimal(v); }

    // ---- Value Object ----

    @Test
    void positiveAmountRejectsZeroNegativeAndNull() {
        assertThrows(IllegalArgumentException.class, () -> PositiveAmount.of("0"));
        assertThrows(IllegalArgumentException.class, () -> PositiveAmount.of("-5"));
        assertThrows(NullPointerException.class, () -> new PositiveAmount(null));
    }

    // ---- Інваріанти ----

    @Test
    void accountRejectsBlankNumberAndNegativeBalance() {
        assertThrows(IllegalArgumentException.class, () -> new CurrentAccount(" ", bd("1")));
        assertThrows(IllegalArgumentException.class, () -> new CurrentAccount("A", bd("-1")));
        assertThrows(IllegalArgumentException.class, () -> new FixedDepositAccount("A", bd("1"), 0));
    }

    @Test
    void withdrawCannotMakeBalanceNegative() {
        CurrentAccount acc = new CurrentAccount("A", bd("100"));
        assertThrows(IllegalStateException.class, () -> acc.withdraw(PositiveAmount.of("100.01")));
        assertEquals(bd("100"), acc.getBalance());
    }

    // ---- Поведінка ----

    @Test
    void depositAndWithdrawChangeBalance() {
        CurrentAccount acc = new CurrentAccount("A", bd("100"));
        acc.deposit(PositiveAmount.of("50"));
        acc.withdraw(PositiveAmount.of("30"));
        assertEquals(bd("120"), acc.getBalance());
    }

    @Test
    void fixedDepositAcceptsDeposits() {
        FixedDepositAccount acc = new FixedDepositAccount("D", bd("1000"), 12);
        acc.deposit(PositiveAmount.of("10"));
        assertEquals(bd("1010"), acc.getBalance());
        assertEquals(12, acc.getTermMonths());
    }

    // ---- LSP ----

    @Test
    void fixedDepositIsNotWithdrawable() {
        assertFalse(WithdrawableAccount.class.isAssignableFrom(FixedDepositAccount.class));
        assertThrows(NoSuchMethodException.class,
                () -> FixedDepositAccount.class.getMethod("withdraw", PositiveAmount.class));
    }

    @Test
    void everyWithdrawableSubtypeHonoursTheBaseContract() {
        WithdrawableAccount[] accounts = { new CurrentAccount("A", bd("100")) };
        for (WithdrawableAccount account : accounts) {
            new BankService().processMonthlyFee(account, PositiveAmount.of("10"));
            assertEquals(bd("90"), account.getBalance());
        }
    }

    @Test
    void allAccountsSupportBaseDeposit() {
        BankAccount[] accounts = { new CurrentAccount("A", bd("0")), new FixedDepositAccount("D", bd("0"), 6) };
        for (BankAccount account : accounts) {
            account.deposit(PositiveAmount.of("5"));
            assertEquals(bd("5"), account.getBalance());
        }
    }
}

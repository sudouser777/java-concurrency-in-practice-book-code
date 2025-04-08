package chapter10;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * DynamicOrderDeadlock
 * <p/>
 * Dynamic lock-ordering deadlock
 *
 * @author Brian Goetz and Tim Peierls
 */
public class DynamicOrderDeadlock {
    // Warning: deadlock-prone!
    public static void transferMoney(Account fromAccount,
                                     Account toAccount,
                                     DollarAmount amount)
            throws InsufficientFundsException {
        synchronized (fromAccount) {
            synchronized (toAccount) {
                if (fromAccount.getBalance().compareTo(amount) < 0)
                    throw new InsufficientFundsException();
                else {
                    fromAccount.debit(amount);
                    toAccount.credit(amount);
                }
            }
        }
    }

    static class DollarAmount implements Comparable<DollarAmount> {
        private int balance;

        public DollarAmount(int amount) {
            balance = amount;
        }

        public DollarAmount add(DollarAmount d) {
            balance += d.balance;
            return this;
        }

        public DollarAmount subtract(DollarAmount d) {
            balance -= d.balance;
            return this;
        }

        public int compareTo(DollarAmount dollarAmount) {
            return Integer.compare(balance, dollarAmount.balance);
        }
    }

    static class Account {
        private static final AtomicInteger sequence = new AtomicInteger();
        private final int acctNo;
        private DollarAmount balance;

        public Account() {
            acctNo = sequence.incrementAndGet();
            balance = new DollarAmount(100);
        }

        void debit(DollarAmount d) {
            balance = balance.subtract(d);
        }

        void credit(DollarAmount d) {
            balance = balance.add(d);
        }

        DollarAmount getBalance() {
            return balance;
        }

        int getAcctNo() {
            return acctNo;
        }
    }

    static class InsufficientFundsException extends Exception {
    }
}
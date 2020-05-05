package com.p1.myaggregate1;

import com.cqrs.aggregates.AggregateExecutionException;
import com.cqrs.commands.CommandSubscriberByMap;
import com.cqrs.testing.BddAggregateTestHelper;
import com.p1.myaggregate1.commands.DepositMoney;
import com.p1.myaggregate1.commands.WithdrawMoney;
import com.p1.myaggregate1.events.MoneyDeposited;
import com.p1.myaggregate1.events.MoneyWithdrawn;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyAggregate1Test {

    @Test
    void handleDeposit() {
        assertDoesNotThrow(() -> {
            BddAggregateTestHelper helper = new BddAggregateTestHelper(new CommandSubscriberByMap());
            helper.onAggregate(new BankAccount())
                .when(new DepositMoney("123", 100))
                .then(new MoneyDeposited("123", 100));
        });
    }

    @Test
    void handleWithdrawMoneyException() {
        assertThrows(AggregateExecutionException.class, () -> {
            BddAggregateTestHelper helper = new BddAggregateTestHelper(new CommandSubscriberByMap());
            helper.onAggregate(new BankAccount())
                .when(new WithdrawMoney("123", 100))
                .then();
        }, "There are not enough money in the bank account");
    }
}
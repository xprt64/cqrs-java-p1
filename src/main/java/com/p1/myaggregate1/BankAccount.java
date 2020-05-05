package com.p1.myaggregate1;

import com.cqrs.annotations.CommandHandler;
import com.cqrs.base.Aggregate;
import com.p1.myaggregate1.commands.DepositMoney;
import com.p1.myaggregate1.commands.WithdrawMoney;
import com.p1.myaggregate1.events.MoneyDeposited;
import com.p1.myaggregate1.events.MoneyWithdrawn;

public class BankAccount extends Aggregate {

    private float balance = 0;

    @CommandHandler
    void handle(WithdrawMoney command) {
        if (balance - command.amount >= 0) {
            emit(new MoneyWithdrawn(command.aggregateId, command.amount));
        } else {
            throw new UnsupportedOperationException("There are not enough money in the bank account");
        }
    }

    @CommandHandler
    void handle(DepositMoney command) {
        emit(new MoneyDeposited(command.aggregateId, command.amount));
    }

    void apply(MoneyWithdrawn event) {
        balance -= event.amount;
    }

    void apply(MoneyDeposited event) {
        balance += event.amount;
    }
}

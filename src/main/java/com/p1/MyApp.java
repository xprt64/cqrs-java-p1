package com.p1;


import com.cqrs.commands.CommandDispatcher;
import com.cqrs.commands.CommandRejectedByValidators;
import com.p1.myaggregate1.commands.DepositMoney;
import com.p1.myaggregate1.commands.WithdrawMoney;
import com.p1.readmodels.AccountBalancesReadModel;
import com.p1.readmodels.TotalMoneyInBankReadModel;

import javax.inject.Inject;
import javax.inject.Named;

@Named
class MyApp {

    private final CommandDispatcher commandDispatcher;
    private final AccountBalancesReadModel accountBalancesReadModel;
    private final TotalMoneyInBankReadModel totalMoneyInBankReadModel;

    @Inject
    public MyApp(
        CommandDispatcher commandDispatcher,
        AccountBalancesReadModel accountBalancesReadModel,
        TotalMoneyInBankReadModel totalMoneyInBankReadModel
    ) {
        this.commandDispatcher = commandDispatcher;
        this.accountBalancesReadModel = accountBalancesReadModel;
        this.totalMoneyInBankReadModel = totalMoneyInBankReadModel;
    }

    public void run() throws Exception {
        System.out.println("## Sending command agg 1");
        commandDispatcher.dispatchCommand(new DepositMoney("1", 500), null);
        commandDispatcher.dispatchCommand(new WithdrawMoney("1", 100), null);
        commandDispatcher.dispatchCommand(new WithdrawMoney("1", 200), null);
        System.out.println("## Sending command agg 2");
        commandDispatcher.dispatchCommand(new DepositMoney("2", 500), null);
        commandDispatcher.dispatchCommand(new WithdrawMoney("2", 100), null);
        System.out.println("## Bank account balances:");
        System.out.println(accountBalancesReadModel.loadAll());
        System.out.println("## Total money in bank:" + totalMoneyInBankReadModel.getTotal());
        commandDispatcher.dispatchCommand(new WithdrawMoney("1", 200), null);
        commandDispatcher.dispatchCommand(new WithdrawMoney("2", 400), null);
        System.out.println("## Bank account balances:");
        System.out.println(accountBalancesReadModel.loadAll());
        System.out.println("## Total money in bank:" + totalMoneyInBankReadModel.getTotal());
        try {
            commandDispatcher.dispatchCommand(new DepositMoney("2", 500), null);
        } catch (CommandRejectedByValidators e) {
            System.out.println(e.toString()); //expected to happen
        }
    }
}
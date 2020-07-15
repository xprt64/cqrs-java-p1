package com.p1;


import com.cqrs.annotations.QuestionSubscriber;
import com.cqrs.commands.CommandDispatcher;
import com.cqrs.commands.CommandRejectedByValidators;
import com.cqrs.questions.Asker;
import com.p1.myaggregate1.commands.DepositMoney;
import com.p1.myaggregate1.commands.WithdrawMoney;
import com.p1.questions.HowMuchMoneyIsInBank;
import com.p1.readmodels.AccountBalancesReadModel;

import javax.inject.Named;

@Named
class MyApp {

    private final CommandDispatcher commandDispatcher;
    private final AccountBalancesReadModel accountBalancesReadModel;
    private final Asker asker;

    public MyApp(
        CommandDispatcher commandDispatcher,
        AccountBalancesReadModel accountBalancesReadModel,
        Asker asker
    ) {
        this.commandDispatcher = commandDispatcher;
        this.accountBalancesReadModel = accountBalancesReadModel;
        this.asker = asker;
    }

    public void run() throws Exception {
        System.out.println("## Sending command agg 1");
        commandDispatcher.dispatchCommand(new DepositMoney("1", 500));
        commandDispatcher.dispatchCommand(new WithdrawMoney("1", 100));
        commandDispatcher.dispatchCommand(new WithdrawMoney("1", 200));
        System.out.println("## Sending command agg 2");
        commandDispatcher.dispatchCommand(new DepositMoney("2", 500));
        commandDispatcher.dispatchCommand(new WithdrawMoney("2", 100));
        System.out.println("## Bank account balances:");
        System.out.println(accountBalancesReadModel.loadAll());
        commandDispatcher.dispatchCommand(new WithdrawMoney("1", 200));
        commandDispatcher.dispatchCommand(new WithdrawMoney("2", 400));
        System.out.println("## Bank account balances:");
        System.out.println(accountBalancesReadModel.loadAll());
        System.out.println("## Total money in bank:" + asker.askAndReturn(new HowMuchMoneyIsInBank()).answer);
        try {
            commandDispatcher.dispatchCommand(new DepositMoney("2", 500));
        } catch (CommandRejectedByValidators e) {
            System.out.println(e.toString()); //expected to happen
        }
    }

    @QuestionSubscriber
    public void whenChanged(HowMuchMoneyIsInBank question){
        System.out.println("(Total money in bank changed:" + question.answer + ")");
    }
}
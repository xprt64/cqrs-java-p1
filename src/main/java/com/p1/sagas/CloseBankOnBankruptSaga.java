package com.p1.sagas;

import com.cqrs.annotations.CommandValidator;
import com.cqrs.annotations.OnceEventHandler;
import com.cqrs.annotations.QuestionSubscriber;
import com.cqrs.events.MetaData;
import com.p1.myaggregate1.commands.DepositMoney;
import com.p1.myaggregate1.commands.WithdrawMoney;
import com.p1.myaggregate1.events.MoneyDeposited;
import com.p1.myaggregate1.events.MoneyWithdrawn;
import com.p1.questions.HowMuchMoneyIsInBank;

import javax.inject.Named;

@Named
public class CloseBankOnBankruptSaga {

    private float total = 0;
    private boolean bankClosed = false;

    @OnceEventHandler
    void on(MoneyWithdrawn event, MetaData metaData) {
        total -= event.amount;
        if (total <= 0) {
            System.out.println("!!! Bank closed due to insufficient capital.");
            bankClosed = true;
        }
    }

    @OnceEventHandler
    void on(MoneyDeposited event) {
        total += event.amount;
    }

//    @QuestionSubscriber
//    public void whenChanged(HowMuchMoneyIsInBank question){
//        if(question.answer <= 0){
//            bankClosed = true;
//        };
//    }

    @CommandValidator
    void validate(WithdrawMoney command) throws UnsupportedOperationException {
        if (bankClosed) {
            throw new UnsupportedOperationException("Money could not be withdrawn from this bank: Bank is closed.");
        }
    }

    @CommandValidator
    void validate(DepositMoney command) throws UnsupportedOperationException {
        if (bankClosed) {
            throw new UnsupportedOperationException("Money could not be deposited to this bank: Bank is closed.");
        }
    }
}

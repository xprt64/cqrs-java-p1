package com.p1.readmodels;

import com.cqrs.annotations.EventHandler;
import com.cqrs.annotations.QuestionAnswerer;
import com.cqrs.events.MetaData;
import com.cqrs.questions.QuestionPublisher;
import com.p1.myaggregate1.events.MoneyDeposited;
import com.p1.myaggregate1.events.MoneyWithdrawn;
import com.p1.questions.HowMuchMoneyIsInBank;

import javax.inject.Named;

@Named
public class TotalMoneyInBankReadModel {

    private final QuestionPublisher questionPublisher;
    private float total = 0;

    public TotalMoneyInBankReadModel(
        QuestionPublisher questionPublisher
    ) {
        this.questionPublisher = questionPublisher;
    }

    @EventHandler
    void on(MoneyWithdrawn event, MetaData metaData){
        total -= event.amount;
        questionPublisher.publishAnsweredQuestion(new HowMuchMoneyIsInBank(total));
    }

    @EventHandler
    void on(MoneyDeposited event){
        total += event.amount;
        questionPublisher.publishAnsweredQuestion(new HowMuchMoneyIsInBank(total));
    }

    public float getTotal(){
        return total;
    }

    @QuestionAnswerer
    public HowMuchMoneyIsInBank whenAsked(HowMuchMoneyIsInBank question){
        return question.answered(total);
    }
}

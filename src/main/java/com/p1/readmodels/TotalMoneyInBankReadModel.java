package com.p1.readmodels;

import com.cqrs.annotations.EventHandler;
import com.cqrs.events.MetaData;
import com.p1.myaggregate1.events.MoneyDeposited;
import com.p1.myaggregate1.events.MoneyWithdrawn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TotalMoneyInBankReadModel {

    public float total = 0;

    @EventHandler
    void on(MoneyWithdrawn event, MetaData metaData){
        total -= event.amount;
    }

    @EventHandler
    void on(MoneyDeposited event){
        total += event.amount;
    }

    public float getTotal(){
        return total;
    }
}
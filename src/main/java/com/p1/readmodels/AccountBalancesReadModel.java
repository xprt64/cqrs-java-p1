package com.p1.readmodels;

import com.cqrs.annotations.EventHandler;
import com.cqrs.events.MetaData;
import com.p1.myaggregate1.events.MoneyDeposited;
import com.p1.myaggregate1.events.MoneyWithdrawn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class AccountBalancesReadModel {

    public static final String TABLE_NAME = "AccountBalancesReadModel";

    private final ReadModelPersistence<Float> persistence;

    @Autowired
    public AccountBalancesReadModel(ReadModelPersistence<Float> persistence) {
        this.persistence = persistence;
    }

    @EventHandler
    void on(MoneyWithdrawn event, MetaData metaData) {
        System.out.println(String.format("[%s] - %.0f", event.accountId, event.amount));
        persistence.mutate(TABLE_NAME, event.accountId, (balance) -> balance - event.amount, () -> 0.0f);
    }

    @EventHandler
    void on(MoneyDeposited event, MetaData metaData) {
        System.out.println(String.format("[%s] + %.0f", event.accountId, event.amount));
        persistence.mutate(TABLE_NAME, event.accountId, (balance) -> balance + event.amount, () -> 0.0f);
    }

    public HashMap<String, Float> loadAll(){
        return persistence.loadAll(TABLE_NAME);
    }
}

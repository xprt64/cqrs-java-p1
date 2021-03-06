package com.p1.readmodels;

import com.cqrs.annotations.EventHandler;
import com.cqrs.events.MetaData;
import com.p1.myaggregate1.events.MoneyDeposited;
import com.p1.myaggregate1.events.MoneyWithdrawn;

import javax.inject.Named;
import java.util.HashMap;

@Named
public class AccountBalancesReadModel {

    public static final String TABLE_NAME = "AccountBalancesReadModel";

    private final ReadModelPersistence<Float> persistence;

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

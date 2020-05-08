package com.p1.myaggregate1.events;

import com.cqrs.base.Event;

public class MoneyWithdrawn implements Event {

    public final String accountId;
    public final float amount;

    public MoneyWithdrawn(String accountId, float amount) {
        this.accountId = accountId;
        this.amount = amount;
    }
}

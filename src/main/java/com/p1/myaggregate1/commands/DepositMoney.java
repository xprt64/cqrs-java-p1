package com.p1.myaggregate1.commands;

import com.cqrs.base.Command;

import java.security.InvalidParameterException;

public class DepositMoney implements Command {

    public final String aggregateId;
    public final float amount;

    public DepositMoney(String aggregateId, float amount) {
        if(amount <= 0){
            // low-level business rule
            throw new InvalidParameterException("You cannot withdraw a negative amount of money");
        }
        this.aggregateId = aggregateId;
        this.amount = amount;
    }

    public String getAggregateId() {
        return aggregateId;
    }
}

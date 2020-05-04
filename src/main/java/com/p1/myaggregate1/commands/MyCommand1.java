package com.p1.myaggregate1.commands;

import com.cqrs.base.Command;

public class MyCommand1 implements Command {

    public final String aggregateId;
    public final int someInt;

    public MyCommand1(String aggregateId, int someInt) {
        this.aggregateId = aggregateId;
        this.someInt = someInt;
    }

    public String getAggregateId() {
        return aggregateId;
    }
}

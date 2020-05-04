package com.p1.myaggregate1.events;

import com.cqrs.base.Event;

public class MyEvent1 implements Event {

    public final String aggregateId;
    public final int someInt;

    public MyEvent1(String aggregateId, int someInt) {
        this.aggregateId = aggregateId;
        this.someInt = someInt;
    }
}

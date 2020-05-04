package com.p1.myaggregate1;

import com.cqrs.base.Aggregate;
import com.cqrs.annotations.CommandHandler;
import com.cqrs.events.MetaData;
import com.p1.myaggregate1.commands.MyCommand1;
import com.p1.myaggregate1.events.MyEvent1;

public class MyAggregate1 extends Aggregate {

    private int someInt;
    private String id = null;

    @CommandHandler
    void handle10(MyCommand1 command) {
        if (true || id != null) {
            emit(new MyEvent1(command.aggregateId, command.someInt));
        }
    }

    void apply(MyEvent1 event) {
        someInt = event.someInt;
        id = event.aggregateId;
    }
}

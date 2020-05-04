package com.p1;


import com.cqrs.commands.CommandDispatcher;
import com.p1.myaggregate1.commands.MyCommand1;
import com.p1.readmodels.ReadModel1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
class MyApp{

    private final CommandDispatcher commandDispatcher;

    private final ReadModel1 readModel;

    @Autowired
    public MyApp(CommandDispatcher commandDispatcher, ReadModel1 readModel) {
        this.commandDispatcher = commandDispatcher;
        this.readModel = readModel;
    }

    public void run() throws Exception{
        System.out.println("## Sending command agg 1");
        commandDispatcher.dispatchCommand(new MyCommand1("1", 100), null);
        commandDispatcher.dispatchCommand(new MyCommand1("1", 200), null);
        commandDispatcher.dispatchCommand(new MyCommand1("1", 300), null);
        System.out.println("## Sending command agg 2");
        commandDispatcher.dispatchCommand(new MyCommand1("2", 100), null);
        System.out.println("## ReadModel entities:");
        System.out.println(readModel.loadAll());
        System.out.println("gata v2");
    }
}
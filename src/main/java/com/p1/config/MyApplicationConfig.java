package com.p1.config;

import com.cqrs.aggregates.EventSourcedAggregateRepository;
import com.cqrs.commands.*;
import com.cqrs.events.*;
import com.cqrs.event_store.memory.InMemoryEventStore;
import com.cqrs.sql_event_store.SqlEventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.security.NoSuchAlgorithmException;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
@ComponentScan("com.p1")
public class MyApplicationConfig {

    @Autowired
    ApplicationContext applicationContext;

    @Bean
    CommandDispatcher commandDispatcher() throws SQLException, NoSuchAlgorithmException {
        final SqlEventStore eventStore = new SqlEventStore(
            DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/eventstore", "root", "pw"),
            "events_p1"
        );
        //eventStore.dropStore();
        eventStore.createStore();
        return new DefaultCommandDispatcher(
            new CommandSubscriberByMap(),
            new CommandApplier(),
            new EventSourcedAggregateRepository(
                eventStore
            ),
            new DefaultSideEffectsDispatcher(
                new CompositeEventDispatcher(
                    new EventDispatcherBySubscriber(
                        new EventSubscriberByMap(
                            new AnnotatedReadModelEventHandlersMap(),
                            eventListenerClass -> applicationContext.getBean(eventListenerClass),
                            errorReporter()
                        )
                    ),
                    new EventDispatcherBySubscriber(
                        new EventSubscriberByMap(
                            new AnnotatedOnceEventHandlersMap(),
                            eventListenerClass -> applicationContext.getBean(eventListenerClass),
                            errorReporter()
                        )
                    )
                )

            )
        );
    }

    private ErrorReporter errorReporter() {
        return new ErrorReporter() {
            @Override
            public void reportEventDispatchError(
                Object eventListener,
                String listenerClass,
                String methodName,
                EventWithMetaData eventWithMetaData,
                Throwable throwable
            ) {
                System.out.println(
                    "Error dispatching event " +
                    eventWithMetaData.event.getClass().getCanonicalName() + " to " +
                    listenerClass + "." + methodName + " " + throwable.getClass().toString() + " " + throwable.getMessage()
                );
                throwable.printStackTrace();
            }
        };
    }
}

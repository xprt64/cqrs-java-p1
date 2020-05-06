package com.p1.config;

import com.cqrs.aggregates.EventSourcedAggregateRepository;
import com.cqrs.commands.*;
import com.cqrs.events.*;
import com.cqrs.infrastructure.AbstractFactory;
import com.cqrs.sql_event_store.SqlEventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
@ComponentScan("com.p1")
public class MyApplicationConfig {

    final ApplicationContext applicationContext;

    @Inject
    public MyApplicationConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    CommandDispatcher commandDispatcher() throws SQLException, NoSuchAlgorithmException {

        final SqlEventStore eventStore = eventStore();
        eventStore.dropStore();
        eventStore.createStore();
        AbstractFactory abstractFactory = applicationContext::getBean;
        return new CommandDispatcherWithValidator(
            new DefaultCommandDispatcher(
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
                                abstractFactory,
                                errorReporter()
                            )
                        ),
                        new EventDispatcherBySubscriber(
                            new EventSubscriberByMap(
                                new AnnotatedOnceEventHandlersMap(),
                                abstractFactory,
                                errorReporter()
                            )
                        )
                    )
                )
            ),
            new CommandValidatorBySubscriber(
                new CommandValidatorSubscriberByMap(
                    abstractFactory,
                    new AnnotatedCommandValidatorsMap()
                )
            )
        );
    }

    private SqlEventStore eventStore() throws SQLException {
        System.out.println(String.format("Connecting to %s", System.getenv("P1_DB_URL")));
        return new SqlEventStore(
            DriverManager.getConnection(
                System.getenv("P1_DB_URL"), System.getenv("P1_DB_USER"), System.getenv("P1_DB_PW")),
            "events_p1"
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
                    listenerClass + "." + methodName + " " + throwable.getClass().toString() + " " +
                    throwable.getMessage()
                );
                throwable.printStackTrace();
            }
        };
    }
}

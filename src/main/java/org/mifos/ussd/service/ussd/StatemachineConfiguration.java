package org.mifos.ussd.service.ussd;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mifos.ussd.service.ussd.action.UssdResponseRenderAction;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableStateMachineFactory
public class StatemachineConfiguration extends StateMachineConfigurerAdapter<UssdState, UssdEvent> {

    private final UssdResponseRenderAction ussdResponseRenderAction;

    @Override
    public void configure(StateMachineTransitionConfigurer<UssdState, UssdEvent> transitions) throws Exception {
        transitions.withExternal()
                   .source(UssdState.INITIAL_STATE).target(UssdState.MAIN_MENU_STATE).event(UssdEvent.MAIN_MENU_EVENT).action(ussdResponseRenderAction)
                   .source(UssdState.MAIN_MENU_STATE).target(UssdState.WITHDRAW_MENU_STATE).event(UssdEvent.WITHDRAW_MENU_EVENT).action(ussdResponseRenderAction)
                   .source(UssdState.MAIN_MENU_STATE).target(UssdState.TRANSFER_MENU_STATE).event(UssdEvent.TRANSFER_MENU_EVENT).action(ussdResponseRenderAction)
                   .source(UssdState.MAIN_MENU_STATE).target(UssdState.LOAN_MENU_STATE).event(UssdEvent.LOAN_MENU_EVENT).action(ussdResponseRenderAction)
                   .source(UssdState.MAIN_MENU_STATE).target(UssdState.SAVINGS_MENU_STATE).event(UssdEvent.SAVINGS_MENU_EVENT).action(ussdResponseRenderAction)
        ;
    }

    @Override
    public void configure(StateMachineStateConfigurer<UssdState, UssdEvent> states) throws Exception {
        states.withStates()
              .initial(UssdState.INITIAL_STATE)
              .states(EnumSet.allOf(UssdState.class))
              .end(UssdState.END_STATE);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<UssdState, UssdEvent> config) throws Exception {
        StateMachineListenerAdapter adapter = new StateMachineListenerAdapter<UssdState, UssdEvent>() {
            @Override
            public void stateChanged(State from, State to) {
                log.info("stateChanged(from: {}, to: {})", from + "", to + "");
            }
        };
        config.withConfiguration()
              .autoStartup(false)
              .listener(adapter);
    }
}

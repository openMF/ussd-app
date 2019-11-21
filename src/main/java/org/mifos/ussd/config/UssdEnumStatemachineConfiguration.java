package org.mifos.ussd.config;

import org.mifos.ussd.service.UssdEvent;
import org.mifos.ussd.service.UssdState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachineFactory
public class UssdEnumStatemachineConfiguration extends StateMachineConfigurerAdapter<UssdState, UssdEvent> {
    private static final Logger logger = LoggerFactory.getLogger(UssdEnumStatemachineConfiguration.class);

    @Override
    public void configure(StateMachineTransitionConfigurer<UssdState, UssdEvent> transitions) throws Exception {
        super.configure(transitions);
        //TODO: configure transitions
    }

    @Override
    public void configure(StateMachineStateConfigurer<UssdState, UssdEvent> states) throws Exception {
        super.configure(states);
        //Todo: Configure states
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<UssdState, UssdEvent> config) throws Exception {
        config.withConfiguration()
                .autoStartup(false);
        //Todo: Configure
    }
}

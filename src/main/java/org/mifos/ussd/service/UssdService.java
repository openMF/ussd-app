package org.mifos.ussd.service;

import org.apache.commons.text.StringEscapeUtils;
import org.mifos.ussd.common.exception.SessionNotFoundException;
import org.mifos.ussd.config.AppConfig;
import org.mifos.ussd.config.AppConstants;
import org.mifos.ussd.domain.Menu;
import org.mifos.ussd.domain.Response;
import org.mifos.ussd.domain.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.access.StateMachineAccess;
import org.springframework.statemachine.access.StateMachineFunction;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UssdService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final StateMachineFactory<UssdState, UssdEvent> stateMachineFactory;
    private final SessionService sessionService;
    private final AppConfig appConfig;

    public UssdService(StateMachineFactory<UssdState, UssdEvent> stateMachineFactory, SessionService sessionService, AppConfig appConfig) {
        this.stateMachineFactory = stateMachineFactory;
        this.sessionService = sessionService;
        this.appConfig = appConfig;
    }

    public Response process(String sessionId, String msisdn, String input, boolean isNew) {
        Response response = new Response();
        //Todo: Write code to handle transitions
        if (isNew) {
            String text = createUssdSession(sessionId, msisdn);
            response.setMessage(StringEscapeUtils.unescapeJava(text));
            response.setContinue(true);
        } else {
            build(sessionId);
        }

        return response;
    }

    private String createUssdSession(String sessionId, String msisdn) {
        logger.info("{\"event\":\"CREATE_SESSION\", \"sessionId\":\"{}\", \"msisdn\":\"{}\"}", sessionId, msisdn);
        Session session = new Session();
        session.setSessionId(sessionId);
        session.setMsisdn(msisdn);

        UssdState initialMenuState = appConfig.getInitialMenuState();
        session.setContextData(AppConstants.CURRENT_STATE_SESSION_KEY, initialMenuState);

        sessionService.createOrUpdateSession(session);

        Menu menu = appConfig.getMenu().get(initialMenuState);
        String menuText = menu.getTextTemplate();

        logger.info("{\"event\":\"DISPLAY_MENU\", \"menuState\":\"{}\", \"menu\":\"{}\"}", initialMenuState, menuText);
        return menuText;
    }

    private StateMachine<UssdState, UssdEvent> build(String sessionId) {
        Session session = sessionService.findSessionBySessionId(sessionId).orElseThrow(()-> new SessionNotFoundException("Could not find session with id: "+sessionId));

        StateMachine<UssdState, UssdEvent> stateMachine = this.stateMachineFactory.getStateMachine(sessionId);
        stateMachine.stop();

        stateMachine.getStateMachineAccessor()
                .doWithAllRegions(new StateMachineFunction<StateMachineAccess<UssdState, UssdEvent>>() {
                    @Override
                    public void apply(StateMachineAccess<UssdState, UssdEvent> sma) {
                        sma.addStateMachineInterceptor(new StateMachineInterceptorAdapter<UssdState, UssdEvent>() {

                            /***
                             * Update state pre state change
                             *
                             * @param state
                             * @param message
                             * @param transition
                             * @param stateMachine
                             */
                            @Override
                            public void preStateChange(State<UssdState, UssdEvent> state, Message<UssdEvent> message,
                                                       Transition<UssdState, UssdEvent> transition,
                                                       StateMachine<UssdState, UssdEvent> stateMachine) {
                                Optional.ofNullable(message).ifPresent(msg -> {
                                    Optional.ofNullable((String) msg.getHeaders().get(AppConstants.SESSION_ID_HEADER))
                                            .ifPresent(sessionId -> {
                                                sessionService.findSessionBySessionId(sessionId).ifPresent(session -> {
                                                    session.setContextData(AppConstants.CURRENT_STATE_SESSION_KEY, state.getId());
                                                    sessionService.createOrUpdateSession(session);
                                                });
                                            });
                                });
                            }
                        });

                        sma.resetStateMachine(new DefaultStateMachineContext<UssdState, UssdEvent>(
                                (UssdState) session.getContextData(AppConstants.CURRENT_STATE_SESSION_KEY), null, null, null));
                    }
                });
        return stateMachine;
    }
}

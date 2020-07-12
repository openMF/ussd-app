package org.mifos.ussd.service.ussd;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mifos.ussd.common.exception.SessionNotFoundException;
import org.mifos.ussd.config.AppConfig;
import org.mifos.ussd.config.AppConstants;
import org.mifos.ussd.domain.Response;
import org.mifos.ussd.domain.Session;
import org.mifos.ussd.service.SessionService;
import org.mifos.ussd.service.ussd.interceptor.UssdStateChangeInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UssdService {

    private final StateMachineFactory<UssdState, UssdEvent> stateMachineFactory;
    private final SessionService sessionService;
    private final AppConfig appConfig;
    private final UssdStateChangeInterceptor ussdStateChangeInterceptor;

    public Response process(String sessionId, String msisdn, String input, boolean isNew) {
        if (isNew) {
            createUssdSession(sessionId, msisdn);
        }
        StateMachine<UssdState, UssdEvent> stateMachine = this.build(sessionId, input);
        UssdState currentState = stateMachine.getState().getId();
        AppConfig.Menu currentStateMenu = appConfig.getMenu().get(currentState);
        UssdEvent ussdEvent = currentStateMenu.getEventTrigger(input);

        sendEvent(sessionId, stateMachine, ussdEvent);

        return (Response) stateMachine.getExtendedState().getVariables().get(AppConstants.MENU_RESPONSE_KEY);
    }

    private void sendEvent(String sessionId, StateMachine<UssdState, UssdEvent> sm, UssdEvent event) {
        sm.sendEvent(MessageBuilder.withPayload(event)
                                   .setHeader(AppConstants.SESSION_ID_HEADER, sessionId)
                                   .build());
    }

    private void createUssdSession(String sessionId, String msisdn) {
        log.info("event=CREATE_SESSION, sessionId={}, msisdn={}}", sessionId, msisdn);
        Session session = new Session();
        session.setSessionId(sessionId);
        session.setMsisdn(msisdn);

        session.setContextData(AppConstants.CURRENT_STATE_SESSION_KEY, UssdState.INITIAL_STATE);

        sessionService.createOrUpdateSession(session);
    }

    private StateMachine<UssdState, UssdEvent> build(String sessionId, String input) {
        Session session = sessionService.findSessionBySessionId(sessionId).orElseThrow(() -> new SessionNotFoundException("Could not find session with id: " + sessionId));

        StateMachine<UssdState, UssdEvent> stateMachine = this.stateMachineFactory.getStateMachine(sessionId);
        stateMachine.stop();

        stateMachine.getStateMachineAccessor()
                    .doWithAllRegions(sma -> {
                        sma.addStateMachineInterceptor(ussdStateChangeInterceptor);
                        sma.resetStateMachine(new DefaultStateMachineContext<>(
                            (UssdState) session.getContextData(AppConstants.CURRENT_STATE_SESSION_KEY), null, null, null));
                    });
        stateMachine.start();
        stateMachine.getExtendedState().getVariables().put(AppConstants.MENU_INPUT_KEY, input);
        return stateMachine;
    }
}

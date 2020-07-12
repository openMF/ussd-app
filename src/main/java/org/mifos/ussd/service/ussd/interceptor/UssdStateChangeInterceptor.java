package org.mifos.ussd.service.ussd.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mifos.ussd.common.exception.SessionNotFoundException;
import org.mifos.ussd.config.AppConstants;
import org.mifos.ussd.domain.Session;
import org.mifos.ussd.service.SessionService;
import org.mifos.ussd.service.ussd.UssdEvent;
import org.mifos.ussd.service.ussd.UssdState;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.mifos.ussd.config.AppConstants.SESSION_ID_HEADER;

@Slf4j
@RequiredArgsConstructor
@Component
public class UssdStateChangeInterceptor extends StateMachineInterceptorAdapter<UssdState, UssdEvent> {

    private final SessionService sessionService;

    /***
     * Update state in session data before state change
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
            String sessionId = (String) msg.getHeaders().get(SESSION_ID_HEADER);
            Session session = sessionService.findSessionBySessionId(sessionId).orElseThrow(() -> new SessionNotFoundException("Could not find session with id: " + sessionId));

            session.setContextData(AppConstants.CURRENT_STATE_SESSION_KEY, state.getId());
            sessionService.createOrUpdateSession(session);
        });
    }
}

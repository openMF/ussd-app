package org.mifos.ussd.service.ussd.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.mifos.ussd.config.AppConfig;
import org.mifos.ussd.config.AppConstants;
import org.mifos.ussd.domain.Response;
import org.mifos.ussd.domain.StateType;
import org.mifos.ussd.service.TemplateHelper;
import org.mifos.ussd.service.ussd.UssdEvent;
import org.mifos.ussd.service.ussd.UssdState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class UssdResponseRenderAction implements Action<UssdState, UssdEvent> {

    private final AppConfig appConfig;
    private final TemplateHelper templateHelper;

    @Override
    public void execute(StateContext<UssdState, UssdEvent> stateContext) {
        UssdState newUssdState = stateContext.getTarget().getId();
        AppConfig.Menu newStateMenu = appConfig.getMenu().get(newUssdState);
        String text = newStateMenu.getTextTemplate();
        Map<String, Object> menuParams = new HashMap<>();
        menuParams.put("accounts", Arrays.asList("123423344", "44555555555", "8879800808"));
        String renderedText = templateHelper.render(newUssdState.name(), text, menuParams);
        Response response = Response.builder().message(StringEscapeUtils.unescapeJava(renderedText))
                                 .terminate(newStateMenu.getStateType().equals(StateType.TERMINATE))
                                 .build();
        stateContext.getExtendedState().getVariables().put(AppConstants.MENU_RESPONSE_KEY, response);
    }
}

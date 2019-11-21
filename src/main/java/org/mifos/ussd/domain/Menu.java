package org.mifos.ussd.domain;

import org.mifos.ussd.service.UssdState;

import java.util.HashMap;
import java.util.Map;

public class Menu {
    private StateType stateType;
    private String textTemplate;
    private Map<String, String> stateChoices =new HashMap<>();
    private UssdState nextState;

    public StateType getStateType() {
        return stateType;
    }

    public void setStateType(StateType stateType) {
        this.stateType = stateType;
    }

    public String getTextTemplate() {
        return textTemplate;
    }

    public void setTextTemplate(String textTemplate) {
        this.textTemplate = textTemplate;
    }

    public Map<String, String> getStateChoices() {
        return stateChoices;
    }

    public void setStateChoices(Map<String, String> stateChoices) {
        this.stateChoices = stateChoices;
    }

    public UssdState getNextState() {
        return nextState;
    }

    public void setNextState(UssdState nextState) {
        this.nextState = nextState;
    }
}

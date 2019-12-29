package org.mifos.ussd.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Override
    public String toString() {
        String str = "";

        try {
            str = new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return str;
    }
}

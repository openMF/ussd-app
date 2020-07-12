package org.mifos.ussd.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.mifos.ussd.domain.StateType;
import org.mifos.ussd.service.ussd.UssdEvent;
import org.mifos.ussd.service.ussd.UssdState;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Validated
@ConfigurationProperties(prefix = "mifos-ussd")
@Component
public class AppConfig {

    @NotNull
    private Integer maximumUSSDSessionIdleAgeSeconds;

    @NotNull
    private Map<UssdState, @NotNull Menu> menu;

    public int getMaximumUSSDSessionIdleAgeSeconds() {
        return maximumUSSDSessionIdleAgeSeconds;
    }

    public void setMaximumUSSDSessionIdleAgeSeconds(int maximumUSSDSessionIdleAgeSeconds) {
        this.maximumUSSDSessionIdleAgeSeconds = maximumUSSDSessionIdleAgeSeconds;
    }

    public Map<UssdState, Menu> getMenu() {
        return menu;
    }

    public void setMenu(Map<UssdState, Menu> menu) {
        this.menu = menu;
    }

    @Data
    public static class Menu {
        @NotNull
        private StateType stateType;

        private String textTemplate;

        @Getter(AccessLevel.PRIVATE)
        private Map<String, @NotNull UssdEvent> eventTriggerChoices = new HashMap<>();

        @Getter(AccessLevel.PRIVATE)
        private UssdEvent eventTrigger;

        public UssdEvent getEventTrigger(String input) {
            switch (this.stateType) {
                case INPUT:
                    return this.eventTrigger;
                case CHOICE:
                    return this.eventTriggerChoices.getOrDefault(input, UssdEvent.UNKNOWN_EVENT);
                case TERMINATE:
                    return UssdEvent.TERMINATE_EVENT;
                default:
                    return UssdEvent.UNKNOWN_EVENT;
            }
        }
    }
}

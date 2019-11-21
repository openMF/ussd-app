package org.mifos.ussd.config;

import org.mifos.ussd.domain.Menu;
import org.mifos.ussd.service.UssdState;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@ConfigurationProperties(prefix = "mifos-ussd")
@Component
public class AppConfig {
    private int maximumUSSDSessionIdleAge;
    private UssdState initialMenuState;
    private Map<UssdState, Menu> menu;

    public int getMaximumUSSDSessionIdleAge() {
        return maximumUSSDSessionIdleAge;
    }

    public void setMaximumUSSDSessionIdleAge(int maximumUSSDSessionIdleAge) {
        this.maximumUSSDSessionIdleAge = maximumUSSDSessionIdleAge;
    }

    public UssdState getInitialMenuState() {
        return initialMenuState;
    }

    public void setInitialMenuState(UssdState initialMenuState) {
        this.initialMenuState = initialMenuState;
    }

    public Map<UssdState, Menu> getMenu() {
        return menu;
    }

    public void setMenu(Map<UssdState, Menu> menu) {
        this.menu = menu;
    }
}

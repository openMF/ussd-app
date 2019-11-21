package org.mifos.ussd.provider;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class UssdProviderFactory implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public UssdProvider getUssdProvider(String providerName) {
        return (UssdProvider) applicationContext.getBean(providerName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

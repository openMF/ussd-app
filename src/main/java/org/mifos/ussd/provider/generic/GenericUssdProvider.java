package org.mifos.ussd.provider.generic;

import org.mifos.ussd.domain.Response;
import org.mifos.ussd.provider.UssdProvider;
import org.mifos.ussd.service.UssdService;
import org.springframework.stereotype.Component;

@Component("generic")
public class GenericUssdProvider extends UssdProvider {
    private static final String PROVIDER = "generic";
    private final UssdService ussdService;

    public GenericUssdProvider(UssdService ussdService) {
        this.ussdService = ussdService;
    }

    @Override
    public Response handleUSSDRequest(String sessionId, String msisdn, String input, boolean isNew) {
        return ussdService.process(sessionId, msisdn, input, isNew);
    }
}

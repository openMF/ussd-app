package org.mifos.ussd.provider.generic;

import org.mifos.ussd.domain.Response;
import org.mifos.ussd.provider.UssdProvider;
import org.mifos.ussd.service.ussd.UssdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("generic")
public class GenericUssdProvider extends UssdProvider {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String PROVIDER = "generic";
    private final UssdService ussdService;

    public GenericUssdProvider(UssdService ussdService) {
        this.ussdService = ussdService;
    }

    @Override
    public Response handleUSSDRequest(String sessionId, String msisdn, String input, boolean isNew) {
        logger.info("{\"event\":\"HANDLE_USSD_REQUEST\", \"sessionId\":\"{}\", \"msisdn\":\"{}\", \"input\":\"{}\", \"isNew\":\"{}\", \"provider\":\"{}\"}", sessionId, msisdn, input, isNew, PROVIDER);
        return ussdService.process(sessionId, msisdn, input, isNew);
    }
}

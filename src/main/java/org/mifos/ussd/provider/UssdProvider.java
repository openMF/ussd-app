package org.mifos.ussd.provider;

import org.mifos.ussd.domain.Response;

public abstract class UssdProvider {

    public abstract Response handleUSSDRequest(String sessionId, String msisdn, String input, boolean isNew);
}

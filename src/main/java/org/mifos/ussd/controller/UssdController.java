package org.mifos.ussd.controller;

import org.mifos.ussd.domain.Response;
import org.mifos.ussd.provider.UssdProvider;
import org.mifos.ussd.provider.UssdProviderFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ussd")
public class UssdController {

    private final UssdProviderFactory ussdProviderFactory;

    public UssdController(UssdProviderFactory ussdProviderFactory) {
        this.ussdProviderFactory = ussdProviderFactory;
    }

    @RequestMapping(
        value = "generic",
        method = RequestMethod.POST,
        produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> ussdHandler(@RequestParam("sessionId") String sessionId,
                                              @RequestParam("msisdn") String msisdn,
                                              @RequestParam("input") String input,
                                              @RequestParam("type") Integer type) {
        UssdProvider ussdProvider = ussdProviderFactory.getUssdProvider("generic");
        Response response = ussdProvider.handleUSSDRequest(sessionId, msisdn, input, (type == 1));

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("x-continue", response.isTerminate() ? "END" : "CONT");

        return ResponseEntity.ok().headers(responseHeaders).body(response.getMessage());
    }
}

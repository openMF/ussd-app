package org.mifos.ussd.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;

/**
 * Created by Antony on 7/25/2016.
 */
@Data
@Builder
public class Response {
    private boolean terminate;
    private String message;
}

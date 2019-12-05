package org.mifos.ussd.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Antony on 7/31/2016.
 */
@RedisHash("USSD_SESSION")
public class Session implements Serializable {
    @Id
    private String sessionId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date dateCreated;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastModified;
    private Map<String, Object> data;
    private String msisdn;

    public Session() {
        data = new HashMap<>();
        setContextData("WAIT_FOR_INPUT_KEY", false);
        setContextData("WAIT_FOR_SECURED_NEXT_KEY", false);
        setContextData("PIN_RETRY_COUNT", 0);
        Date date = new Date();
        dateCreated = date;
        lastModified = date;
    }

    public Object getContextData(String key) {
        return data.get(key);
    }


    public void setContextData(String key, Object value) {
        Object object = data.get(key);

        if (object != null) {
            data.remove(key);
        }
        data.put(key, value);
    }

    public Object removeContextData(String key) {
        return data.remove(key);
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
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

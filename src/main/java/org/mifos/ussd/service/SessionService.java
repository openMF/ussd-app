package org.mifos.ussd.service;

import org.mifos.ussd.domain.Session;

import java.util.Optional;

public interface SessionService {

    public Session createOrUpdateSession(Session session);

    public Optional<Session> findSessionBySessionId(String sessionId);

    public void delete(Session session);

    public void clearOldSessions();
}

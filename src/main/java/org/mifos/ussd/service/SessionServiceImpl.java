package org.mifos.ussd.service;

import org.mifos.ussd.config.AppConfig;
import org.mifos.ussd.config.AppConstants;
import org.mifos.ussd.domain.Session;
import org.mifos.ussd.repository.session.SessionRepository;
import org.mifos.ussd.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionServiceImpl implements SessionService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SessionRepository sessionRepository;
    private final AppConfig appConfig;

    public SessionServiceImpl(SessionRepository sessionRepository, AppConfig appConfig) {
        this.sessionRepository = sessionRepository;
        this.appConfig = appConfig;
    }

    @Override
    public Session createOrUpdateSession(Session session) {
        return sessionRepository.save(session);
    }

    @Override
    public Optional<Session> findSessionBySessionId(String sessionId) {
        return sessionRepository.findById(sessionId);
    }

    @Override
    public void delete(Session session) {
        sessionRepository.delete(session);
    }

    @Override
    @Scheduled(fixedDelayString = "${mifos-ussd.clearIdleSessionsFixedDelay:5000}")
    public void clearOldSessions() {
        sessionRepository.findAll().forEach(session -> {
            int age = DateUtil.getAge(session.getLastModified());
            if ((age > appConfig.getMaximumUSSDSessionIdleAge()) ||
                    (session.getContextData(AppConstants.CURRENT_STATE_SESSION_KEY).equals(UssdState.END_STATE))) {
                delete(session);
                logger.info("{\"event\":\"{}\", \"age\":{}, \"session\":{}}", AppConstants.LOG_EVICT_SESSION_EVENT,
                        age, session);
            }
        });
    }
}

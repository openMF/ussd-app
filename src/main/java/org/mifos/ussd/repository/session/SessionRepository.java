package org.mifos.ussd.repository.session;

import org.mifos.ussd.domain.Session;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by Antony on 7/31/2016.
 */
@Repository
public interface SessionRepository extends CrudRepository<Session, String> {

}

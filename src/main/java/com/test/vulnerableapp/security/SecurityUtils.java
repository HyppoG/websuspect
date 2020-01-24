/**
 * 
 */
package com.test.vulnerableapp.security;

import com.test.vulnerableapp.model.FailedLoginAttempt;
import com.test.vulnerableapp.model.User;
import com.test.vulnerableapp.operations.UserOperation;
import com.test.vulnerableapp.repositories.BaseDAO;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component("securityUtils")
public class SecurityUtils extends BaseDAO {

	private static final Logger logger = Logger.getLogger(SecurityUtils.class);

	@Value("${security.login.maxAttempts}")
	String maxAttempts;
	@Value("${security.login.within}")
	String within;
	@Value("${security.login.lockTime}")
	String blockFor;
	@Autowired
	UserOperation userOperation;

	public void logLoginFailure(String username) {
		FailedLoginAttempt log = new FailedLoginAttempt();
		log.setFailedAt(new Date());
		log.setUsername(username);
		Session session = getCurrentSession();
		Transaction tx = session.beginTransaction();
		session.save(log);
		tx.commit();
		checkAccountLocked(username);
		session.close();

	}

	public void checkAccountLocked(String username) {
		// TODO : Fix me
		User user = userOperation.retrieveByUsername(username).get();
		if (user != null && user.isLocked()) {
			// throw account locked and let go.
			throw new AccountLockedException();
		}

		Calendar checkFrom = Calendar.getInstance();
		checkFrom.add(Calendar.SECOND, Integer.parseInt(within) * -1);

		Session session = getCurrentSession();
		Transaction tx = session.beginTransaction();
		Long attempts = 0l;
		try {
			Query query = session.createQuery(
					"select count(fla.id) from FailedLoginAttempt fla where fla.failedAt >= :checkFrom and fla.username = :username");
			query.setParameter("checkFrom", checkFrom.getTime());
			query.setParameter("username", username);
			attempts = (Long) query.uniqueResult();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			if (tx.getStatus() != TransactionStatus.ACTIVE)
				tx.commit();
			session.close();
		}

		if (attempts >= Integer.parseInt(maxAttempts)) {
			throw new AccountLockedException();
		}
	}

}

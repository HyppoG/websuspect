package com.test.vulnerableapp.persistance;

import java.util.ArrayList;
import java.util.List;

import com.test.vulnerableapp.repositories.BaseDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.test.vulnerableapp.model.Role;
import com.test.vulnerableapp.util.ApplicationException;

@Component
public class RoleDAOImpl extends BaseDAO implements RoleDAO {
	private static final Logger logger = LoggerFactory.getLogger(RoleDAOImpl.class);

	public void saveRole(Role role) {
		Session session = null;
		Transaction tx = null;
		try {
			session = getCurrentSession();
			tx = session.beginTransaction();
			session.persist(role);
			tx.commit();
		} catch (Exception e) {
			logger.error("Error at saving role information ", e);
			if (tx != null) {
				tx.rollback();
				throw new ApplicationException(1111, "Database Exception.");
			}
		} finally {
			session.close();
		}
	}

	public void update(Role role) {

	}

	public void delete(Role role) {
		Session session = null;
		Transaction tx = null;
		try {
			session = getCurrentSession();
			tx = session.beginTransaction();
			session.delete(role);
			tx.commit();
		} catch (Exception e) {
			logger.error("Error at delete role information ", e);
			if (tx != null) {
				tx.rollback();
				throw new ApplicationException(1111, "Database Exception.");
			}
		} finally {
			session.close();
		}
	}

	public Role getRoleById(long id) {
		Session session = null;
		Transaction tx = null;
		Role role = null;
		try {
			session = getCurrentSession();
			tx = session.beginTransaction();
			role = session.get(Role.class, id);
			tx.commit();
		} catch (Exception e) {
			logger.error("Error at getRoleById function ", e);
			if (tx != null) {
				tx.rollback();
				throw new ApplicationException(1111, "Database Exception.");
			}
		} finally {
			session.close();
		}
		return role;
	}

	public Role getRoleByName(String roleName) {
		Session session = null;
		Transaction tx = null;
		Role role = null;
		try {
			session = getCurrentSession();
			tx = session.beginTransaction();
			Criteria crit = session.createCriteria(Role.class);
			Criterion role_name = Restrictions.eq("role_name", roleName);
			crit.add(role_name);
			crit.setMaxResults(1);
			role = (Role) crit.uniqueResult();
			tx.commit();
		} catch (Exception e) {
			logger.error("Error at getRoleByName function ", e);
			if (tx != null) {
				tx.rollback();
				throw new ApplicationException(1111, "Database Exception.");
			}
		} finally {
			session.close();
		}
		return role;
	}

	@SuppressWarnings("unchecked")
	public List<Role> getAllRoleList() {
		Session session = null;
		Transaction tx = null;
		List<Role> roleList = new ArrayList<Role>();
		try {
			session = getCurrentSession();
			tx = session.beginTransaction();
			roleList = (List<Role>) session.createQuery("from Role").list();
			tx.commit();
		} catch (Exception e) {
			logger.error("Error at getAllRoleList function ", e);
			if (tx != null) {
				tx.rollback();
				throw new ApplicationException(1111, "Database Exception.");
			}
		} finally {
			session.close();
		}
		return roleList;
	}

}
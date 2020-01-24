package com.test.vulnerableapp.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaseDAO {

	@Autowired
	SessionFactory sessionFactory;

	public BaseDAO() {
		super();
	}

	public Session getCurrentSession() {

		return sessionFactory.openSession();
	}

}
package com.test.vulnerableapp.repositories;

import com.test.vulnerableapp.model.UserInfo;
import com.test.vulnerableapp.util.ApplicationException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryCustomImpl extends BaseDAO implements UserRepositoryCustom {
    @Override
    public Optional<UserInfo> findByUsername(String username) {
        Session session = null;
        Transaction tx = null;
        UserInfo user = null;
        try {
            session = getCurrentSession();
            tx = session.beginTransaction();
            Query query = session.createQuery("from UserInfo user where user.username='" + username + "'");
            user = (UserInfo) query.uniqueResult();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
                throw new ApplicationException(1111, e.toString());
            }
        } finally {
            session.close();
        }
        Optional<UserInfo> oUser = Optional.ofNullable(user);
        return oUser;
    }

    public List<UserInfo> getAllByBirthday() {
        Session session = null;
        Transaction tx = null;
        Long count = Long.valueOf(0);
        LocalDateTime date = LocalDateTime.now();
        int today_month = date.getMonthValue();
        int today_day = date.getDayOfMonth();
        List<UserInfo> userList = new ArrayList<UserInfo>();
        try {
            session = getCurrentSession();
            tx = session.beginTransaction();
            Query query = session.createQuery(
                    "from UserInfo user where MONTH(user.birthday)=:today_month AND DAY(user.birthday)=:today_day");
            query.setParameter("today_month", today_month);
            query.setParameter("today_day", today_day);
            userList = (List<UserInfo>) query.list();
            tx.commit();
        } catch (Exception e) {
            //logger.error("Error at getUserListByTodayDate funtion ", e);
            if (tx != null) {
                tx.rollback();
                throw new ApplicationException(1111, "Database Exception.");
            }
        } finally {
            session.close();
        }
        return userList;
    }
}

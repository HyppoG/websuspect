package com.test.vulnerableapp.repositories;

import com.test.vulnerableapp.util.AppUtil;
import com.test.vulnerableapp.util.ApplicationException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

public class OldUserRepositoryCustomImpl implements OldUserRepositoryCustom {

    @Autowired
    DataSource dataSource;

    @Override
    public String findByUsername(String username) {
        String name = AppUtil.filterInput(username);
        String user_name = null;
        try {
            JdbcTemplate vJdbcTemplate = new JdbcTemplate(dataSource);
            user_name = vJdbcTemplate.queryForObject(
                    "Select username from users user where user.username='" + name + "'", String.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            //logger.error("Error at findUserByName funtion ", e);
        } finally {
        }
        return user_name;
    }

    public Boolean existsByUsernameAndPassword(String username, String password) {
        // "Sanitize" the input for password, no need for the username as we have
        // retrieved it from the database
        // What could happen ?
        String pass_word = AppUtil.sanitizeInput(password);
        long user_id = 0;
        Boolean isvalidUser = false;

        try {
            JdbcTemplate vJdbcTemplate = new JdbcTemplate(dataSource);
            user_id = vJdbcTemplate.queryForObject("select id from users u where u.username = '" + username + "' and u.password = '" + pass_word + "'", long.class);
        } catch (EmptyResultDataAccessException e) {
            return false;
        } catch (Exception e) {

        } finally {

        }

        return user_id != 0;
    }
}

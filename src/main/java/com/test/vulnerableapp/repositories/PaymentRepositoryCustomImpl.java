package com.test.vulnerableapp.repositories;

import com.test.vulnerableapp.model.PaymentDetails;
import com.test.vulnerableapp.model.UserInfo;
import com.test.vulnerableapp.util.ApplicationException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaymentRepositoryCustomImpl extends BaseDAO implements PaymentRepositoryCustom {

    private static final Logger logger = LoggerFactory.getLogger(PaymentRepositoryCustomImpl.class);
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override/**
     * Method will save the payment details into the database.
     */
    public boolean savePaymentDetails(PaymentDetails paymentDetails) {
        Session session = null;
        Transaction tx = null;
        boolean isSuccess = true;
        try {
            session = getCurrentSession();
            tx = session.beginTransaction();
            String dml = "insert into paymentDetails (orderId, cardNumber, cardOwner, totalAmount) values (:order_id, ':card_number', ':card_owner', :total_amount)";
            dml = dml.replace(":order_id", String.valueOf(paymentDetails.getOrderId()));
            dml = dml.replace(":card_number", paymentDetails.getCardNumber());
            dml = dml.replace(":card_owner", paymentDetails.getCardOwner());
            dml = dml.replace(":total_amount", String.valueOf(paymentDetails.getTotalAmount()));
            jdbcTemplate.update(dml);
            // To here
            tx.commit();
        } catch (Exception e) {
            logger.error("Error at saving Payment Details information ", e);
            if (tx != null) {
                tx.rollback();
                throw new ApplicationException(1111, "Database Exception.");
            }
            isSuccess = false;
        } finally {
            session.close();
        }
        return isSuccess;
    }
}

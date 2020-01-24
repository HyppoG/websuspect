package com.test.vulnerableapp.repositories;

import com.test.vulnerableapp.model.PaymentDetails;
import com.test.vulnerableapp.model.UserInfo;

import java.util.List;
import java.util.Optional;

public interface PaymentRepositoryCustom {

    boolean savePaymentDetails(PaymentDetails paymentDetails);

}

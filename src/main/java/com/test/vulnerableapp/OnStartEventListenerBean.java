package com.test.vulnerableapp;

import com.test.vulnerableapp.model.UserInfo;
import com.test.vulnerableapp.operations.UserOperation;
import com.test.vulnerableapp.util.AppUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class OnStartEventListenerBean  implements ApplicationListener<ContextRefreshedEvent>{
	
	private static final Logger LOG = LoggerFactory.getLogger(OnStartEventListenerBean.class);

	@Autowired
	private UserOperation userOperation;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		UserInfo admin = new UserInfo();
		admin.setEmail("admin@email.com");
		admin.setIsSiteAdmin(1);
		admin.setUsername("admin");
		admin.setPassword("820aed4531fd6759a019b93b54a88605");
		admin.setRole_id(1001);
		admin.setStatus("ACTIVE");
		
		LocalDate localDate = LocalDate.now();
		
		// Set admin key
		String today = "";
		if(localDate.getMonthValue() < 10) {
			today = "" + localDate.getDayOfMonth() + "0" + localDate.getMonthValue();
		} else {
			today = "" + localDate.getDayOfMonth() + localDate.getMonthValue();
		}

		String key = AppUtil.getEncryptedKey(today + "1989");
		admin.setSuperkey(key);
		
		// Set admin birthday
		localDate.minusYears(40);
		Date birthday = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		admin.setBirthday(birthday);

		userOperation.addUser(admin);
	}
}
package com.test.vulnerableapp.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="userInfo")
public class UserInfo extends User implements Serializable {
	private static final long serialVersionUID = 1L;

	private String superkey;

	public String getSuperkey() {
		return superkey;
	}
	public void setSuperkey(String superkey) {
		this.superkey = superkey;
	}

}
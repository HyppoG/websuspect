/**
 * 
 */
package com.test.vulnerableapp.model;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "FailedLoginAttempt")
public class FailedLoginAttempt {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private BigInteger id;

	@Column(name = "failed_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date failedAt;

	@Column(name = "username")
	private String username;

	/**
	 * @return the id
	 */
	public BigInteger getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(BigInteger id) {
		this.id = id;
	}

	/**
	 * @return the failedAt
	 */
	public Date getFailedAt() {
		return failedAt;
	}

	/**
	 * @param failedAt the failedAt to set
	 */
	public void setFailedAt(Date failedAt) {
		this.failedAt = failedAt;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

}

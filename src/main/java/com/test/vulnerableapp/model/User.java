package com.test.vulnerableapp.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import com.test.vulnerableapp.util.AppConstants;

@MappedSuperclass
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String username;
    private String password;
    private long role_id;
    private String status;
    private int isSiteAdmin;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date birthday;
    @Transient
    private String password2;

    @Column(name = "locked_until", columnDefinition = "TIMESTAMP DEFAULT '1999-01-01 00:00:01'")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lockedUntil = new GregorianCalendar(1999, Calendar.JANUARY, 1).getTime();

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getRole_id() {
        return role_id;
    }

    public void setRole_id(long role_id) {
        this.role_id = role_id;
    }

    public int getIsSiteAdmin() {
        if (isSiteAdmin == 0) isSiteAdmin = AppConstants.NOT;
        return isSiteAdmin;
    }

    public void setIsSiteAdmin(int isSiteAdmin) {
        this.isSiteAdmin = isSiteAdmin;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String toString() {
        return "id " + id + " username " + username + " email " + email;
    }

    public Date getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(Date lockedUntil) {
        this.lockedUntil = lockedUntil;
    }

    public boolean isLocked() {
        return lockedUntil == null || lockedUntil.after(new Date());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isValid() {
        return !(StringUtils.isEmpty(username) || StringUtils.isEmpty(email) || StringUtils.isEmpty(id))
                && Objects.nonNull(birthday);
    }

}
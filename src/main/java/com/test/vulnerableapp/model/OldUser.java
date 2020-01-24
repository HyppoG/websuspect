package com.test.vulnerableapp.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class OldUser extends User implements Serializable {
	private static final long serialVersionUID = 1L;
}
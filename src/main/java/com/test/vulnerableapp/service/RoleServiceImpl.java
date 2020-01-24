package com.test.vulnerableapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.test.vulnerableapp.model.Role;
import com.test.vulnerableapp.persistance.RoleDAO;

@Component
public class RoleServiceImpl implements RoleService {
	@Autowired
	RoleDAO roledao;

	public void saveRole(Role role) {
		
	}

	public void update(Role role) {
		
	}

	public void delete(Role role) {
		
	}

	public Role getRoleById(long id) {
		roledao.getRoleById(id);
		return null;
	}

	public Role getRoleByName(String roleName) {
		roledao.getRoleByName(roleName);
		return null;
	}

	public List<Role> getAllRoleList() {
		return roledao.getAllRoleList();
	}

}
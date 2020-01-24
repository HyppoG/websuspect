package com.test.vulnerableapp.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.test.vulnerableapp.model.Role;

@Transactional
public interface RoleService {
	void saveRole(Role role);

	void update(Role role);

	void delete(Role role);

	Role getRoleById(long id);

	Role getRoleByName(String roleName);

	List<Role> getAllRoleList();
}
package com.test.vulnerableapp.persistance;

import java.util.List;

import com.test.vulnerableapp.model.Role;

public interface RoleDAO {
	void saveRole(Role role);
	void update(Role role);
	void delete(Role role);
	Role getRoleById(long id);
	Role getRoleByName(String roleName);
	List<Role> getAllRoleList();
}
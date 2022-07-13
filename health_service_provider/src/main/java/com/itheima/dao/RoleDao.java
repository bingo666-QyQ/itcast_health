package com.itheima.dao;

import com.itheima.pojo.Role;

import java.util.List;
import java.util.Set;

public interface RoleDao {
    Set<Role> findRolesByUserId(Integer userId);
}

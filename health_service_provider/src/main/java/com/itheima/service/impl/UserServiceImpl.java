package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;

    @Override
    public User findByUserName(String username) {
        User user = userDao.findByUserName(username);//user的基本信息
        if(user == null){
            return null;
        }
        Integer userId = user.getId();
        //根据用户查询相应的角色
        Set<Role> roles = roleDao.findRolesByUserId(userId);
        for (Role role : roles) {
            //根据角色查询相应的权限
            Integer roleId = role.getId();
            Set<Permission> permissions = permissionDao.findPermissionsByRoleId(roleId);
            role.setPermissions(permissions);
        }
        user.setRoles(roles);
        return user;
    }

//    @Override
//    public String findUserImg(String userName) {
//        String img = userDao.findImgByUsername();
//        System.out.println("img:_______" + img);
//        return img;
//    }

}

package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {

    //使用dubbo通过 网络 远程调用 服务提供方(zookeeper服务中心)获取数据库中的用户信息
    @Reference
    private UserService userService;

    public static String getUserName() {
        return userName;
    }

    private static String userName;
    private static String img;

    public static String getImg() {
        return img;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        userName = username;
        User user = userService.findByUserName(username);
        img = user.getImg();
        if(user == null){
            return null;
        }
        List<GrantedAuthority> list = new ArrayList<>();
        //遍历角色给用户授予角色
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            list.add(new SimpleGrantedAuthority(role.getKeyword()));
            Set<Permission> permissions = role.getPermissions();
            //遍历权限给用户授予权限
            for (Permission permission : permissions) {
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),list);
    }
}

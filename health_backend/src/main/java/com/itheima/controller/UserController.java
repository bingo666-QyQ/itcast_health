package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.SpringSecurityUserService;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

//    @Autowired
//    private SpringSecurityUserService springSecurityUserService;

//    @Reference
//    private UserService userService;

    @RequestMapping("/getUsername")
    public Result getUsername(){
        Map<String,Object> map = new HashMap<>();
        String userName = SpringSecurityUserService.getUserName();
        String img = SpringSecurityUserService.getImg();
        map.put("userName", userName);
        map.put("img", img);
        if(userName != null){
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,map);
        }
        return new Result(false, MessageConstant.GET_USERNAME_FAIL);
    }
}

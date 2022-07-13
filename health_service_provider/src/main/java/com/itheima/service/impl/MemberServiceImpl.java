package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        //给密码进行加密再放入数据库中
        String password = member.getPassword();
        if(password != null){
            password = MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberCountByMonth(List<String> date) {
        List<Integer> list = new ArrayList<>();
        for(String m : date){
//            m = m + "-31";//格式：2019.04.31
            String year = m.substring(0,4);
            String month = m.substring(5);
            Map<String,Object> map = new HashMap<>();
            map.put("year",year);
            map.put("month",month);
            Integer count = memberDao.findMemberCountBeforeDate(map);
            System.out.println(count);
            list.add(count);
        }
        return list;
    }

}

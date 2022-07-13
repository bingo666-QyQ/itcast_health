package com.itheima.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    public Result order(Map map) {

        // 1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        String orderDate = (String) map.get("orderDate");
        String setmealId = (String) map.get("setmealId");
        Long count = orderDao.findByOrderDate(orderDate);
        if(count <= 0){
            //没有预约设置，不能预约
            return new Result(false,MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        // 2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        Integer isZero = orderDao.findIsFull(orderDate);
        if(isZero == 0){
            return new Result(false,MessageConstant.ORDER_FULL);
        }

        // 3、检查用户是否重复预约（同一个用户 在同一天 预约了 同一个套餐），如果是重复预约则无法完成再次预约
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        //防止重复预约
        if(member != null){
            Integer memberId = member.getId();
            int OrderSetmealId = Integer.parseInt(setmealId);
            Order order = new Order(memberId, (Date) map.get("orderDate"),null,null,OrderSetmealId);
            List<Order> list = orderDao.findByCondition(order);
            if(list != null && list.size() > 0){
                //已经完成了预约，不能重复预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }

        // 4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
        if(member == null){
            //当前用户不是会员，需要添加到会员表
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }
        // 5、预约成功，更新当日的已预约人数
        //可以预约，设置预约人数加一
        orderDao.setReservationsAndOne(orderDate);
        //保存预约信息到预约表
        Order order = null;
        try {
            order = new Order(member.getId(),
                    DateUtils.parseString2Date(orderDate),
                    (String)map.get("orderType"),
                    Order.ORDERSTATUS_NO,
                    Integer.parseInt(setmealId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        orderDao.add(order);

        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }
}

package com.itheima.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService{

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void add(List<OrderSetting> data) {
        if(data != null && data.size() > 0){
            for (OrderSetting orderSetting : data) {
                long CountByOrderDate = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if(CountByOrderDate > 0 ){
                    //已经进行了预约设置，更新预约
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else {
                    //没有进行了预约设置，添加预约
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    @Override
    public List<Map> getOrderSettingByMonth(String orderDate) {  //date:yyyy-MM
        //select * from t_ordersetting where YEAR(orderDate) = '2020' and MONTH(orderDate) = '04';
        String year = orderDate.substring(0,4);  //左闭右开
        String month = orderDate.substring(5);
        Map<String,String> queryMap = new HashMap<>();
        queryMap.put("year", year);
        queryMap.put("month", month);
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(queryMap);

        List<Map> returnList = new ArrayList<>();
        if(list != null && list.size() > 0){
            for (OrderSetting orderSetting : list) {
                HashMap<String, Integer> map = new HashMap<>();
                int date = orderSetting.getOrderDate().getDate();//天
                int number = orderSetting.getNumber();//可预约数
                int reservations = orderSetting.getReservations();//已预约数
                map.put("date", date);
                map.put("number", number);
                map.put("reservations", reservations);
                returnList.add(map);
            }
        }
        System.out.println(returnList);
        return returnList;
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {

        Date orderDate = orderSetting.getOrderDate();
        int number = orderSetting.getNumber();
        Map<String,Object> map = new HashMap<>();
        map.put("orderDate", orderDate);
        map.put("number", number);
        orderSettingDao.editNumberByOrderDate(map);
    }
}

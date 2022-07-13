package com.itheima.dao;

import com.itheima.pojo.OrderSetting;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    public void add(OrderSetting orderSetting);
    public void editNumberByOrderDate(OrderSetting orderSetting);
  	public long findCountByOrderDate(Date orderDate);

    List<OrderSetting> getOrderSettingByMonth(Map<String,String> map);

    void editNumberByOrderDate(Map<String,Object> map);
}
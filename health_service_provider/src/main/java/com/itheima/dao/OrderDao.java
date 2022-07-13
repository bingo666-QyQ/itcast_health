package com.itheima.dao;

import com.itheima.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    Long findByOrderDate(String orderDate);

    Integer findIsFull(String orderDate);

    List<Order> findByCondition(Order order);

    void setReservationsAndOne(String orderDate);

    void add(Order order);

    public Integer findOrderCountByDate(String date);
    public Integer findOrderCountAfterDate(String date);
    public Integer findVisitsCountByDate(String date);
    public Integer findVisitsCountAfterDate(String date);
    public List<Map> findHotSetmeal();
}

package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetMealService {
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult queryPage(QueryPageBean queryPageBean);

    List<Setmeal> findAll();

    Setmeal getById(Integer id);

    List<Map<String, Object>> findSetmealCount();

}

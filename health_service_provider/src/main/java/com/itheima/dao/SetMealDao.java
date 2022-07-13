package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetMealDao {
    void add(Setmeal setmeal);

    void setSetMealAndCheckGroup(Map<String, Integer> map);

    Page<Setmeal> findByCondition(String queryString);

    List<Setmeal> findAll();

    List<Integer> findCheckGroupsBySetMealId(Integer id);

    Setmeal findById(Integer id);

    CheckGroup findCheckGroupById(Integer checkGroupId);

    List<Integer> findCheckItemsByCheckGroupId(Integer checkGroupIdForCheckItem);

    CheckItem findCheckItemByCheckItemId(Integer checkItemId);

    List<Map<String, Object>> findSetmealCount();

}

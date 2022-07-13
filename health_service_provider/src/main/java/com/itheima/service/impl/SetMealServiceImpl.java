package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetMealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetMealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.*;


@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealDao setMealDao;
    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${out_put_path}")
    private String outPutPath;

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setMealDao.add(setmeal);
        Integer setMealId = setmeal.getId();

        if(checkgroupIds != null && checkgroupIds.length > 0){
            for (Integer checkgroupId : checkgroupIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("setMealId", setMealId);
                map.put("checkgroupId", checkgroupId);
                setMealDao.setSetMealAndCheckGroup(map);
            }
        }
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());

        //新增套餐后需要重新生成静态页面
        generateMobileStaticHtml();
    }

    //生成静态页面
    public void generateMobileStaticHtml() {
        //准备模板文件中所需的数据
        List<Setmeal> setmealList = this.findAll();
        //生成套餐列表静态页面
        generateMobileSetmealListHtml(setmealList);
        //生成套餐详情静态页面（多个）
        generateMobileSetmealDetailHtml(setmealList);
    }

    //生成套餐列表静态页面
    public void generateMobileSetmealListHtml(List<Setmeal> setmealList) {
        //封装用于生成静态页面所需的数据
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("setmealList", setmealList);
        this.generateHtml("mobile_setmeal.ftl","m_setmeal.html",dataMap);
    }

    //生成套餐详情静态页面（多个）
    public void generateMobileSetmealDetailHtml(List<Setmeal> setmealList) {
        //遍历生成多个详情页面
        for (Setmeal setmeal : setmealList) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            //根据套餐id查询生成每个详情页面
            dataMap.put("setmeal", this.getById(setmeal.getId()));
            this.generateHtml("mobile_setmeal_detail.ftl",
                    "setmeal_detail_"+setmeal.getId()+".html",
                    dataMap);
        }
    }

    //通用生成静态页面的方法
    public void generateHtml(String templateName,String htmlPageName,Map<String, Object> dataMap){
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;
        try {
            // 加载模版文件
            Template template = configuration.getTemplate(templateName);
            // 生成数据
            File docFile = new File(outPutPath + "\\" + htmlPageName);
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
            // 输出文件
            template.process(dataMap, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.flush();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    @Override
    public PageResult queryPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setMealDao.findByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<Setmeal> findAll() {
        return setMealDao.findAll();
    }

    @Override
    public Setmeal getById(Integer id) {
        //查到setmeal的基本信息(未封装检查组)
        Setmeal setmeal = setMealDao.findById(id);
        //查询所有的检查组id
        List<Integer> checkGroupIds = setMealDao.findCheckGroupsBySetMealId(id);
        //根据每一个检查组查询自己对应的检查项，最后封装在每个检查组中
        List<CheckGroup> checkGroupList = new ArrayList<>();
        //遍历每个检查组id，为了得到完整的检查组信息
        for (Integer checkGroupId : checkGroupIds) {
            //查询每个检查组的基本信息,（未封装检查项）
            CheckGroup checkGroup = setMealDao.findCheckGroupById(checkGroupId);
            //得到单个检查组的id
            Integer checkGroupIdForCheckItem = checkGroup.getId();
            //根据id查询所有的检查项的id
            List<Integer> checkItemIds = setMealDao.findCheckItemsByCheckGroupId(checkGroupIdForCheckItem);
            List<CheckItem> checkItemList = new ArrayList<>();
            //遍历每个检查项，得到完整的检查项的信息
            for (Integer checkItemId : checkItemIds) {
                //查询每个检查项的完整信息
                CheckItem checkItem = setMealDao.findCheckItemByCheckItemId(checkItemId);
                //将每个检查项添加到检查项集合中
                checkItemList.add(checkItem);
            }
            //checkGroup封装拥有的检查项集合
            checkGroup.setCheckItems(checkItemList);
            //检查组集合添加单个的完整的检查组
            checkGroupList.add(checkGroup);
        }
        //setmeal封装拥有的检查组集合
        setmeal.setCheckGroups(checkGroupList);
        //返回套餐
        return setmeal;
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {

        return setMealDao.findSetmealCount();
    }
}

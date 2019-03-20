package cn.itcast.core.service.usermanage;

import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.ad.Content;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserManageServiceImpl implements UserManageService {

    @Resource
    private UserDao userDao;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 将用户登录次数储存在redis
     */
    @Override
    public void userActive(String loginName){
        String strActive = (String) redisTemplate.boundValueOps(loginName).get();
        if (strActive==null){
            redisTemplate.boundValueOps(loginName).set("1");
            //设置过期时间
            redisTemplate.boundValueOps(loginName).expire(24, TimeUnit.HOURS);
        }else {
            int i = Integer.parseInt(strActive);
            i++;
            redisTemplate.boundValueOps(loginName).set(i+"");
            //设置过期时间
            redisTemplate.boundValueOps(loginName).expire(24, TimeUnit.HOURS);
        }

    }

    /**
     * 运营商管理用户分页查询
     * @param pageSize
     * @return
     */
    @Override
    public PageResult search(Integer pageNo, Integer pageSize, User user) {
        //设置分页条件
        PageHelper.startPage(pageNo,pageSize);
        //设置查询条件
        UserQuery userQuery = new UserQuery();
        //通过quety封装查询条件
        UserQuery.Criteria criteria = userQuery.createCriteria();
        if (user.getUsername()!=null && !"".equals(user.getUsername())){
            criteria.andUsernameEqualTo(user.getUsername());
        }

        if (user.getPhone()!=null && !"".equals(user.getPhone())){
            criteria.andPhoneEqualTo(user.getPhone());
        }

         if (user.getStatus()!=null && !"".equals(user.getStatus())){
                criteria.andStatusEqualTo(user.getStatus());
         }
        //根据id排序
        userQuery.setOrderByClause("id desc");
        //根据条件查询
        Page<User> page = (Page<User>) userDao.selectByExample(userQuery);
        //根据数据库条件查询的结果
        List<User> rows = page.getResult();
        //活跃度判断

        if (user.getActiveCount()!=null && !"".equals(user.getActiveCount())){
            ArrayList<User> activeUserList = new ArrayList<>();
            for (User userDatabase : rows) {
                String activeRedisCount = (String) redisTemplate.boundValueOps(userDatabase.getUsername()).get();
                if (activeRedisCount !=null && !"".equals(activeRedisCount)){
//                    System.out.println(activeRedisCount);
                    Integer i =Integer.parseInt(activeRedisCount);
                    Integer s =Integer.parseInt(user.getActiveCount());
                    if (i>=s){
                        activeUserList.add(userDatabase);
                    }
                }

            }
            return new PageResult(page.getTotal(),activeUserList);
        }
        //将结果封装进PageResult
        return new PageResult(page.getTotal(),rows);
    }

    /**
     * 运营商冻结（解冻）用户
     * @param ids
     * @param status
     */
    @Transactional
    @Override
    public void updateStatus(Long[] ids, String status) {
        if (ids !=null && !"".equals(ids)){
            User user = new User();
            user.setStatus(status);
            for (Long id : ids) {
                user.setId(id);
                userDao.updateByPrimaryKeySelective(user);
            }
        }
    }

    /**
     * 运营商删除用户
     */
    @Transactional
    @Override
    public void delete(Long[] ids) {
        if (ids!=null && !"".equals(ids)){
            User user = new User();
            for (Long id : ids) {
                user.setId(id);
                userDao.deleteByPrimaryKey(id);
            }
        }
    }


}

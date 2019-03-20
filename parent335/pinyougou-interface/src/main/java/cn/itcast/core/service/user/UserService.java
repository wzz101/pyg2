package cn.itcast.core.service.user;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.ad.Content;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.user.User;
import com.alibaba.dubbo.config.annotation.Service;

import java.util.List;

@Service
public interface UserService {

    /**
     * 用户获取短信验证码
     * @param phone
     */
    public void sendCode(String phone);

    /**
     * 用户注册
     * @param user
     * @param smscode
     */
    public void add(User user, String smscode);

    /**
     * c
     *
     * @param id
     * @return
     */
    public User findById(Long id);
    public void upDate(User user);

    public List<User> findOneByuserName(String userName);
    public PageResult search(String userName, Integer page, Integer rows, Order order);

    /**
     * 查询
     * @param name
     * @return
     */
    public List<Long> findById2(String name);
//
}

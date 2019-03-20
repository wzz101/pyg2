package cn.itcast.core.service.user;

import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderItemQuery;
import cn.itcast.core.pojo.order.OrderQuery;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;
import cn.itcast.core.utils.md5.MD5Util;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private JmsTemplate jmsTemplate;

    @Resource
    private Destination smsDestination;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserDao userDao;


    @Resource
    private OrderDao orderDao;
    @Resource
    private OrderItemDao orderItemDao;
    @Resource
    private ItemDao itemDao;
    @Resource
    private SellerDao sellerDao;
    /**
     * 获取短信验证码
     *
     */
    @Override
    public void sendCode(final String phone) {
        //将获取短信验证码的数据发送到mq中
        //手机号，验证码，签名，模板
        final String code = RandomStringUtils.randomNumeric(6);
        System.out.println("code:"+code);
        //保存验证码到redis中
        redisTemplate.boundValueOps(phone).set(code);
        //设置过期时间
        redisTemplate.boundValueOps(phone).expire(5, TimeUnit.MINUTES);
        jmsTemplate.send(smsDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                //封装map消息体
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("phoneNumbers", phone);
                mapMessage.setString("signName", "阮文");
                mapMessage.setString("templateCode", "SMS_140720901");
                mapMessage.setString("templateParam", "{\"code\":\""+code+"\"}");
                return mapMessage;
            }
        });
    }

    /**
     * 用户注册
     */
    @Transactional
    @Override
    public void add(User user, String smscode) {
        //校验验证码是否正确
        String code = (String) redisTemplate.boundValueOps(user.getPhone()).get();
        if (smscode !=null && !"".equals(smscode) && smscode.equals(code)){
            //对密码加密
            String password = MD5Util.MD5Encode(user.getPassword(), null);
            user.setPassword(password);
            user.setCreated(new Date());
            user.setUpdated(new Date());
            userDao.insertSelective(user);
        }else {
            throw new RuntimeException("输入的验证码不正确");
        }
    }

    /**
     * c
     *
     * @param id
     * @return
     */
    @Override
    public User findById(Long id) {
        return userDao.selectByPrimaryKey(id);
    }

    /**
     * 用户信息更新
     * @param user
     */
    @Transactional
    @Override
    public void upDate(User user) {
        userDao.updateByPrimaryKey(user);
    }

    @Override
    public List<Long> findById2(String name) {
        UserQuery userQuery = new UserQuery();
        userQuery.createCriteria().andUsernameEqualTo(name);
        List<User> users = userDao.selectByExample(userQuery);
        String headPic = users.get(0).getHeadPic();
        if(headPic != null && !"".equals(headPic.trim())) {
            String[] split = headPic.split(",");
            List<Long> longs = new ArrayList<>();
            for (String s : split) {
                long l = Long.parseLong(s);
                longs.add(l);
            }
            return longs;
        }
        return null;
    }


    /**
     * 根据用户名查询用户信息
     * @param userName
     * @return
     */
    @Override
    public List<User> findOneByuserName(String userName) {
        UserQuery userQuery = new UserQuery();
        UserQuery.Criteria criteria = userQuery.createCriteria();
        criteria.andUsernameEqualTo(userName);
        List<User> userList = userDao.selectByExample(userQuery);
        return userList;
    }


    /**
     * 用户订单查询
     * @param userName
     * @param page
     * @param rows
     * @param order
     * @return
     */
    @Override
    public PageResult search(String userName, Integer page, Integer rows, Order order) {

        //设置分页条件
        PageHelper.startPage(page, rows);
        //根据登录用户查询订单
        //根据登录用户id,查询订单信息
        OrderQuery orderQuery = new OrderQuery();
        OrderQuery.Criteria criteria = orderQuery.createCriteria();
        criteria.andUserIdEqualTo(userName);
        if(null != order && StringUtils.isNotBlank(order.getStatus())){
            criteria.andStatusEqualTo(order.getStatus());
        }
        //查询后的订单
        Page<Order> orderList = (Page<Order>) orderDao.selectByExample(orderQuery);

        if (orderList != null && orderList.size() > 0) {
            for (Order myOrder : orderList) {
                //获取订单 id
                Long orderId =myOrder.getOrderId();
                //根据订单id,查询订单详情orderItem
                OrderItemQuery orderItemQuery = new OrderItemQuery();
                OrderItemQuery.Criteria orderItemQueryCriteria = orderItemQuery.createCriteria();
                orderItemQueryCriteria.andOrderIdEqualTo(orderId);
                List<OrderItem> orderItemList = orderItemDao.selectByExample(orderItemQuery);
                if (orderItemList != null && orderItemList.size() > 0) {
                    for (OrderItem orderItem : orderItemList) {
                        //获取item对象
                        Long itemId = orderItem.getItemId();
                        Item item = itemDao.selectByPrimaryKey(itemId);
                        orderItem.setSpellMap(item.getSpecMap());
                        orderItem.setCostPirce(item.getCostPirce());
                        orderItem.setMarketPrice(item.getMarketPrice());
                    }
                }
                //获取order里面的seller_id,
                String sellerId =myOrder.getSellerId();
                //根据商家id,查询商家名称
                Seller seller = sellerDao.selectByPrimaryKey(sellerId);
                myOrder.setOrderItemList(orderItemList);
            }
        }
        //将结果封装到pageResult中返回
        PageResult pageResult = new PageResult(orderList.getTotal(), orderList.getResult());
        return pageResult;
    }


}

package cn.itcast.core.service.user;

import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.utils.md5.MD5Util;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
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
}

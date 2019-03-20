package cn.itcast.core.service.order;




import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.log.PayLogDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.cart.Cart;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.good.GoodsDesc;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;
import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderQuery;
import cn.itcast.core.utils.uniquekey.IdWorker;
import cn.itcast.core.vo.GoodsVo;
import cn.itcast.core.vo.Ordervo;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

    @Service
    public class OrderItemServiceImpl implements OrderItemService {



        @Resource
        private OrderItemDao orderItemDao;

        @Resource
        private OrderDao orderDao;

        @Resource
        private RedisTemplate<String,Object> redisTemplate;

        @Resource
        private IdWorker idWorker;

        @Resource
        private ItemDao itemDao;

        //注入支付日志接口代理对象
        @Resource
        private PayLogDao payLogDao;

        /**
         * 提交订单
         */
        @Transactional





               @Override
             public PageResult searchitem(Integer page, Integer rows, OrderItem orderItem) {
                    // 设置分页条件
                            PageHelper.startPage(page, rows);
                   Page<OrderItem> orderItems = (Page<OrderItem>) orderItemDao.selectByExample(null);
                    return new PageResult(orderItems.getTotal(), orderItems.getResult());
                }




    }



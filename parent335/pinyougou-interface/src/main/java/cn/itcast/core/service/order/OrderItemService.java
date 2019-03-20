package cn.itcast.core.service.order;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.vo.Ordervo;


public interface OrderItemService {



    /**
     * 订单查询
     */


    public PageResult searchitem(Integer page, Integer rows, OrderItem orderItem);





}

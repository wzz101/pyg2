package cn.itcast.core.service.order;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.order.Order;

public interface OrderService {
    /**
     * 提交订单
     */
    public void add(String username, Order order);


    /**
     * c
     * 用户的订单查询
     * @param order
     * @param page
     * @param rows
     * @return
     */


    /**
     * 删除订单
     * @param ids
     */
    public void delete(Long[] ids);
//
}

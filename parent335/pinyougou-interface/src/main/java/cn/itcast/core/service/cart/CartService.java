package cn.itcast.core.service.cart;

import cn.itcast.core.pojo.cart.Cart;
import cn.itcast.core.pojo.item.Item;

import java.util.List;

public interface CartService {
    /**
     * 获取库存对象
     */
    public Item findOne(Long id);

    /**
     * 填充购物车列表需要回显的数据
     */
    List<Cart> setAttributeForCart(List<Cart> cartList);
    /**
     * 将购物车同步到redis中
     */
    void mergeCartList(String username,List<Cart> cartList);

    /**
     * 从redis中取出购物车
     */
    List<Cart> findCartListFromRedis(String username);

}

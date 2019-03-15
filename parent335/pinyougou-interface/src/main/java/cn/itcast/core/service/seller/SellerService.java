package cn.itcast.core.service.seller;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.seller.Seller;

public interface SellerService {
    /**
     * 商家的入住申请
     *
     */
    public void add(Seller seller);

    /**
     *
     * @Title: search
     * @Description: 待审核商家列表查询
     * @param page
     * @param rows
     * @param seller
     * @return
     * @return PageResult
     * @throws
     */
    public PageResult search(Integer page, Integer rows, Seller seller);

    /**
     * 回显商家待审核商家
     */
    public Seller findOne(String sellerId);
    /**
     * 审核商家
     */
    public void updateStatus(String sellerId,String status);
}

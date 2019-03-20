package cn.itcast.core.service.goods;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.vo.GoodsVo;

import java.util.List;

public interface GoodsService {
    /**
     * 保存商品
     */
    public void add(GoodsVo goodsVo);

    /**
     * 商家系统下的商品列表查询
     */
    public PageResult search(Integer page, Integer rows, Goods goods);

    /**
     * 商品回显
     */
    public GoodsVo findOne(Long id);

    /**
     * 更新商品
     */
    public void update(GoodsVo goodsVo);
    /**
     * 运营商系统下的商品列表查询
     */
    public PageResult searchForManager(Integer page, Integer rows, Goods goods);
    /**
     * 审核商品
     */
    public void updateStatus(Long[] ids,String auditStatus);
    /**
     * 删除商品（逻辑删除）
     */
    public void delete(Long[] ids);

    void updateMarketable(Long[] ids, String markeStatus);
    /**
     * 查询全部商品
     */
    public List<Goods> findAll();

}

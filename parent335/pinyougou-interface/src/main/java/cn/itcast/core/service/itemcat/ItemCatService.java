package cn.itcast.core.service.itemcat;

import cn.itcast.core.pojo.item.ItemCat;

import java.util.List;

public interface ItemCatService {
    /**
     * 商品分类的列表查询
     */
    public List<ItemCat> findByParentId(Long parentId);

    /**
     * 回显模板Id
     */
    public ItemCat findOne(Long id);

    /**
     * 查询所有分类
     */
    public List<ItemCat> findAll();
}

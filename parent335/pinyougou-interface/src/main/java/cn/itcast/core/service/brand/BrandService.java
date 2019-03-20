package cn.itcast.core.service.brand;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Brand;

import java.util.List;
import java.util.Map;

public interface BrandService {
    /**
     * 查询所有的品牌
     * @return
     */
    public List<Brand> findAll();

    /**
     * 品牌管理结果分页
     * @param pageNo
     * @param pageSize
     * @return
     */
    public PageResult findPage(Integer pageNo,Integer pageSize);

    /**
     * 条件查询
     * @param pageNo
     * @param pageSize
     * @param brand
     * @return
     */
    public PageResult search(Integer pageNo,Integer pageSize,Brand brand);

    /**
     * 保存品牌
     */
    public void add(Brand brand);

    /**
     * 回显品牌
     */
    public Brand findOne(Long id);

    /**
     * 更新品牌
     */
    public void update(Brand brand);

    /**
     * 删除
     * @param ids
     */
    public void delete(Long[] ids);

    /**
     * 新增模板时需要加载的品牌结果集
     * @return
     */
    List<Map> selectOptionList();
/*
* 审核品牌
* */
    void updateStatus(Long[] ids, String status);
}

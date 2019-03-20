package cn.itcast.core.service.spec;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.vo.SpecVo;

import java.util.List;
import java.util.Map;

public interface SpecService {
    /**
     * 规格列表查询
     * @param page
     * @param rows
     * @param specification
     * @return
     */
    public PageResult search(Integer page, Integer rows, Specification specification);
    //保存规格
    public void add(SpecVo specVo);
    //回显规格
    public SpecVo findOne(Long id);
    //更新规格
    public void update(SpecVo specVo);
    //删除规格
    public void delete(Long[] ids);
    /**
     * 新增模板时加载规格结果集
     * @return
     */
    List<Map> selectOptionList();

/*
* 规格审核
* */
    void updateStatus(Long[] ids, String status);
}

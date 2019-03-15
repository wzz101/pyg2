package cn.itcast.core.service.template;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.template.TypeTemplate;

import java.util.List;
import java.util.Map;

public interface TypeTemplateService {
    //模板列表查询
    public PageResult search (Integer page, Integer rows, TypeTemplate typeTemplate);
    /**
     * 新增模板
     */

    public void add(TypeTemplate typeTemplate);

    /**
     * 新增商品：回显品牌以及扩展属性
     */
    TypeTemplate findOne(Long id);

    /**
     * 新增商品：回显规格以及规格选项
     */
    public List<Map> findBySpecList(Long id);
}

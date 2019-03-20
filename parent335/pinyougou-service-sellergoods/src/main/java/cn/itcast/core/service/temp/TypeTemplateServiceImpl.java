package cn.itcast.core.service.temp;

import cn.itcast.core.dao.specification.SpecificationOptionDao;
import cn.itcast.core.dao.template.TypeTemplateDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.specification.SpecificationOption;
import cn.itcast.core.pojo.specification.SpecificationOptionQuery;
import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.pojo.template.TypeTemplateQuery;
import cn.itcast.core.service.template.TypeTemplateService;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class TypeTemplateServiceImpl implements TypeTemplateService{

    @Resource
    private TypeTemplateDao typeTemplateDao;

    @Resource
    private SpecificationOptionDao specificationOptionDao;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    /**
     * 模板列表查询
     * @param page
     * @param rows
     * @param typeTemplate
     * @return
     */
    @Override
    public PageResult search(Integer page, Integer rows, TypeTemplate typeTemplate) {
        //列表查询过程中将数据同步到redis中
        List<TypeTemplate> list = typeTemplateDao.selectByExample(null);
        if (list != null && list.size()>0){
            for (TypeTemplate template : list) {
                //将品牌结果集存储到内存中
                String brandIds = template.getBrandIds();
                List<Map> brandList = JSON.parseArray(brandIds, Map.class);
                redisTemplate.boundHashOps("brandList").put(template.getId(),brandList);
                //将规格结果集存储到内存中
                List<Map> specList = findBySpecList(template.getId());
                redisTemplate.boundHashOps("specList").put(template.getId(),specList);
            }
        }

        // 1、设置分页条件
        PageHelper.startPage(page, rows);
        // 2、设置查询条件
        TypeTemplateQuery query = new TypeTemplateQuery();
        if(typeTemplate.getName() != null && !"".equals(typeTemplate.getName().trim())){
            query.createCriteria().andNameLike("%" + typeTemplate.getName().trim() + "%");
        }
        query.setOrderByClause("id desc");
        // 3、根据条件查询
        Page<TypeTemplate> p = (Page<TypeTemplate>) typeTemplateDao.selectByExample(query);
        // 4、封装结果集
        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * 保存模板
     * @param typeTemplate
     */
    @Transactional
    @Override
    public void add(TypeTemplate typeTemplate) {

    typeTemplateDao.insertSelective(typeTemplate);
    }

    /**
     * 新增商品：回显品牌以及扩展属性
     * @param id
     * @return
     */
    @Override
    public TypeTemplate findOne(Long id) {
        return typeTemplateDao.selectByPrimaryKey(id);
    }

    /**
     * 新增商品：回显规格以及规格选项
     * @param id
     * @return
     */
    @Override
    public List<Map> findBySpecList(Long id) {
        //通过id 获取到模板
        TypeTemplate typeTemplate = typeTemplateDao.selectByPrimaryKey(id);
        //通过模板获取规格
        //[{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
        String specids = typeTemplate.getSpecIds();
        //将json串 转成对象
        List<Map> specList = JSON.parseArray(specids, Map.class);
        //设置规格选项
        if (specList != null && specList.size()>0){
            for (Map map : specList) {
                //获取规格id
                long specId =Long.parseLong(map.get("id").toString());
                //获取对应的规格选项
                SpecificationOptionQuery query = new SpecificationOptionQuery();
                query.createCriteria().andSpecIdEqualTo(specId);
                List<SpecificationOption> options = specificationOptionDao.selectByExample(query);
                //将规格选项设置到map中
                map.put("options",options);
            }
        }
        return specList;
    }
/*
* 模板审核
* */
    @Override
    public void updateStatus(Long[] ids, String status) {
        TypeTemplate typeTemplate = new TypeTemplate();
        if (ids != null && ids.length > 0) {
            typeTemplate.setStatus(status);

            for ( final Long id : ids) {
                typeTemplate.setId(id);

            }
            typeTemplateDao.updateByPrimaryKeySelective(typeTemplate);
        }
    }
}

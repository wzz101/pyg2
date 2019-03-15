package cn.itcast.core.service.itemcat;

import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemCatQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService{

    @Resource
    private ItemCatDao itemCatDao;

    @Resource
    private RedisTemplate redisTemplate;
    /**
     * 商品列表的分类查询
     * @param parentId
     * @return
     */
    @Override
    public List<ItemCat> findByParentId(Long parentId) {
        //列表查询过程中将数据同步到redis中
        List<ItemCat> list = itemCatDao.selectByExample(null);
        if (list != null && list.size()>0){
            for (ItemCat itemCat : list) {
                redisTemplate.boundHashOps("itemCat").put(itemCat.getName(),itemCat.getTypeId());
            }
        }


        //根据parentid查询
        ItemCatQuery itemCatQuery =new ItemCatQuery();
        itemCatQuery.createCriteria().andParentIdEqualTo(parentId);
        List<ItemCat> itemCats = itemCatDao.selectByExample(itemCatQuery);
        return itemCats;
    }

    /**
     * 回显模板
     * @param id
     * @return
     */
    @Override
    public ItemCat findOne(Long id) {
        return itemCatDao.selectByPrimaryKey(id);
    }

    /**
     * 查询所有分类
     * @return
     */
    @Override
    public List<ItemCat> findAll() {

        return itemCatDao.selectByExample(null);
    }
}

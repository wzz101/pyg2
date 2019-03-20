package cn.itcast.core.service.itemcat;

import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemCatQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    public List<ItemCat> findByItemCat3(Long parentId01) {
        // 1.先从redis缓存中 , 获取三级分类信息!
        List<ItemCat> itemCat01List  = (List<ItemCat>) redisTemplate.boundValueOps("itemCat01").get();

        // 2.若缓存中没有数据 , 从数据库中查询( 并放到缓存中 )
        if (itemCat01List==null){
            // 缓存穿透 -> 请求排队等候.
            synchronized (this){
                // 进行二次校验?
                itemCat01List  = (List<ItemCat>) redisTemplate.boundValueOps("itemCat01").get();
                if (itemCat01List==null){
                    // 创建一个集合 , 存放一级分类
                    itemCat01List = new ArrayList<>();

                    // 根据parent_id = 0 , 获取一级分类信息!
                    List<ItemCat> itemCatList = itemCatDao.selectByParentId(parentId01);
                    for (ItemCat itemCat1 : itemCatList) {
                        // 设置一级分类信息!
                        ItemCat itemCat01 = new ItemCat();
                        itemCat01.setId(itemCat1.getId());
                        itemCat01.setName(itemCat1.getName());
                        itemCat01.setParentId(itemCat1.getParentId());

                        // 根据一级分类的id -> 找到对应的二级分类!
                        List<ItemCat> itemCatList02 = new ArrayList<>();
                        ItemCatQuery itemCatQuery02 = new ItemCatQuery();
                        itemCatQuery02.createCriteria().andParentIdEqualTo(itemCat01.getId());
                        List<ItemCat> itemCat02List = itemCatDao.selectByExample(itemCatQuery02);
                        for (ItemCat itemCat2 : itemCat02List) {
                            // 设置二级分类信息!
                            ItemCat itemCat02 = new ItemCat();
                            itemCat02.setId(itemCat2.getId());
                            itemCat02.setName(itemCat2.getName());
                            itemCat02.setParentId(itemCat2.getParentId());

                            // 根据二级分类的id -> 找到对应的三级分类!
                            List<ItemCat> itemCatList03 = new ArrayList<>();
                            ItemCatQuery itemCatQuery03 = new ItemCatQuery();
                            itemCatQuery03.createCriteria().andParentIdEqualTo(itemCat02.getId());
                            List<ItemCat> itemCat03List = itemCatDao.selectByExample(itemCatQuery03);
                            for (ItemCat itemCat3 : itemCat03List) {
                                itemCatList03.add(itemCat3);
                            }
                            itemCat02.setItemCatList(itemCatList03);  // 二级分类中 添加 三级分类.
                            itemCatList02.add(itemCat02);       // 添加二级分类.

                        }
                        itemCat01.setItemCatList(itemCatList02); // 一级分类中 添加 二级分类!
                        itemCat01List.add(itemCat01);  // 添加一级分类
                    }
                    // 将查询到的数据放入缓存中!
                    redisTemplate.boundValueOps("itemCat01").set(itemCat01List);
                    return itemCat01List;
                }
            }

        }

        // 3.若缓存中有数据 , 直接返回即可!
        List<ItemCat> itemCat01 = (List<ItemCat>) redisTemplate.boundValueOps("itemCat01").get();

        return itemCat01List;
    }
}

package cn.itcast.core.service.goods;

import cn.itcast.core.dao.good.BrandDao;
import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.good.GoodsDescDao;
import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.good.GoodsDesc;
import cn.itcast.core.pojo.good.GoodsQuery;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;
import cn.itcast.core.service.staticpage.StaticPageService;
import cn.itcast.core.vo.GoodsVo;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class GoodsServiceImpl implements GoodsService{
    @Resource
    private GoodsDao goodsDao;

    @Resource
    private GoodsDescDao goodsDescDao;

    @Resource
    private ItemDao itemDao;

    @Resource
    private ItemCatDao itemCatDao;

    @Resource
    private BrandDao brandDao;

    @Resource
    private SellerDao sellerDao;

    @Resource
    private SolrTemplate solrTemplate;

//    @Resource
//    private StaticPageService staticPageService;

    @Resource
    private JmsTemplate jmsTemplate;

    @Resource
    private Destination topicPageAndSolrDestination;

    @Resource
    private Destination queueSolrDeleteDestination;
    /**
     * 保存商品
     * @param goodsVo
     */
    @Transactional
    @Override
    public void add(GoodsVo goodsVo) {
        //保存商品基本信息
        Goods goods = goodsVo.getGoods();
        //初始化商品的审核状态：待审核
        goods.setAuditStatus("0");
        //        goods.setSellerId();      // 在controller中设置
        //返回自增主键的id
        goodsDao.insertSelective(goods);
        //保存商品描述信息
        GoodsDesc goodsDesc =goodsVo.getGoodsDesc();
        goodsDesc.setGoodsId(goods.getId());
        goodsDescDao.insertSelective(goodsDesc);

        //保存商品对应的库存信息
        if ("1".equals(goods.getIsEnableSpec())){//启用规格
            List<Item> itemList = goodsVo.getItemList();

        if (itemList != null && itemList.size()>0){
            for (Item item : itemList) {
                //设置库存的商品标题
                // 标题=spu名称+spu副标题+规格选项名称
                String title = goods.getGoodsName() + " " + goods.getCaption();
                //规格选项在item中
                // 栗子：{"机身内存":"16G","网络":"联通3G"}  [{},{},{}]
                String spec = item.getSpec();
                Map<String, String> map = JSON.parseObject(spec, Map.class);
                Set<Map.Entry<String, String>> entries = map.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    title += " " + entry.getValue();
                }
                item.setTitle(title);
                setAttributeForItem(item, goods, goodsDesc);
                //保存库存
                itemDao.insertSelective(item);
            }
        }
        } else {
            //不启用规格：一个商品可以对应一个库存
            Item item = new Item();
            item.setTitle(goods.getGoodsName() + " " + goods.getCaption());//库存的商品
            item.setPrice(goods.getPrice());//商品价格
            item.setIsDefault("1");//默认的商品
            item.setSpec("{}");//无规格
            item.setNum(8888);//库存量
            setAttributeForItem(item, goods, goodsDesc);
            //保存库存
            itemDao.insertSelective(item);
    }
}

    /**
     * 商家系统下的商品列表查询
     * @param page
     * @param rows
     * @param goods
     * @return
     */
    @Override
    public PageResult search(Integer page, Integer rows, Goods goods) {
        //设置分页条件
        PageHelper.startPage(page,rows);
        //设置查询条件
        GoodsQuery goodsQuery = new GoodsQuery();
        if(goods.getSellerId() != null && !"".equals(goods.getSellerId())){
            goodsQuery.createCriteria().andSellerIdEqualTo(goods.getSellerId());
        }
        // 查询
        Page<Goods> p = (Page<Goods>) goodsDao.selectByExample(goodsQuery);
        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * 商品回显
     * @return
     */
    @Override
    public GoodsVo findOne(Long id) {
        GoodsVo goodsVo = new GoodsVo();
        //商品基本信息
        Goods goods = goodsDao.selectByPrimaryKey(id);
        goodsVo.setGoods(goods);
        //商品描述信息
        GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(id);
        goodsVo.setGoodsDesc(goodsDesc);
        //商品对应的库存信息
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(id);
        List<Item> itemList = itemDao.selectByExample(itemQuery);
        goodsVo.setItemList(itemList);
        return goodsVo;
    }

    /**
     * 更新商品
     * @param goodsVo
     */
    @Transactional
    @Override
    public void update(GoodsVo goodsVo) {
        //更新商品基本信息
        Goods goods = goodsVo.getGoods();
        //修改需要重新审核
        goods.setAuditStatus("0");
        goodsDao.updateByPrimaryKeySelective(goods);
        //更新商品描述信息
        GoodsDesc goodsDesc = goodsVo.getGoodsDesc();
        goodsDescDao.updateByPrimaryKeySelective(goodsDesc);
        //更新库存：先删后加
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(goods.getId());
        itemDao.deleteByExample(itemQuery);
        //保存商品对应的库存信息
        if ("1".equals(goods.getIsEnableSpec())){//启用规格
            List<Item> itemList = goodsVo.getItemList();

            if (itemList != null && itemList.size()>0){
                for (Item item : itemList) {
                    //设置库存的商品标题
                    // 标题=spu名称+spu副标题+规格选项名称
                    String title = goods.getGoodsName() + " " + goods.getCaption();
                    //规格选项在item中
                    // 栗子：{"机身内存":"16G","网络":"联通3G"}  [{},{},{}]
                    String spec = item.getSpec();
                    Map<String, String> map = JSON.parseObject(spec, Map.class);
                    Set<Map.Entry<String, String>> entries = map.entrySet();
                    for (Map.Entry<String, String> entry : entries) {
                        title += " " + entry.getValue();
                    }
                    item.setTitle(title);
                    setAttributeForItem(item, goods, goodsDesc);
                    //保存库存
                    itemDao.insertSelective(item);
                }
            }
        } else {
            //不启用规格：一个商品可以对应一个库存
            Item item = new Item();
            item.setTitle(goods.getGoodsName() + " " + goods.getCaption());//库存的商品
            item.setPrice(goods.getPrice());//商品价格
            item.setIsDefault("1");//默认的商品
            item.setSpec("{}");//无规格
            item.setNum(8888);//库存量
            setAttributeForItem(item, goods, goodsDesc);
            //保存库存
            itemDao.insertSelective(item);
        }
    }

    /**
     * 运营商系统下的商品列表查询
     * @param page
     * @param rows
     * @param goods
     * @return
     */
    @Override
    public PageResult searchForManager(Integer page, Integer rows, Goods goods) {
        //设置分页
        PageHelper.startPage(page,rows);
        //设置查询条件：待审核、未删除
        GoodsQuery goodsQuery = new GoodsQuery();
        GoodsQuery.Criteria criteria = goodsQuery.createCriteria();
        if (goods.getAuditStatus() != null && "".equals(goods.getAuditStatus().trim())){
            criteria.andAuditStatusEqualTo(goods.getAuditStatus().trim());
        }
        //未删除
        criteria.andIsDeleteIsNull();  //代表未删除 已删除 1
        goodsQuery.setOrderByClause("id desc");
        //查询
        Page<Goods> p = (Page<Goods>) goodsDao.selectByExample(goodsQuery);

        return new PageResult(p.getTotal(),p.getResult());
    }

    /**
     * 审核商品
     * @param ids
     *
     */
    @Transactional
    @Override
    public void updateStatus(Long[] ids, String auditStatus) {
        if (ids!=null && ids.length>0){
            Goods goods = new Goods();
            goods.setAuditStatus(auditStatus);
            for (final Long id : ids) {
                goods.setId(id);
                goodsDao.updateByPrimaryKeySelective(goods);
                if ("1".equals(auditStatus)){
                    // 将审核通过后的商品信息保存到索引库中（上架）
//                    isShow(id);
                    //将所有的商品保存到索引库中：为了增加数据量，不是最终的做法
//                    dataImportToSolr();

                    //生成商品详情的静态页
//                    staticPageService.getHtml(id);
                    //将消息（商品id）发送到mq中
                    jmsTemplate.send(topicPageAndSolrDestination, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            //将商品id封装成消息体
                            TextMessage textMessage = session.createTextMessage(String.valueOf(id));

                            return textMessage;
                        }
                    });
                }
            }
        }
    }
    // 将审核通过后的商品信息保存到索引库中（上架）
    private void isShow(Long id) {
        //将该商品对应的库存中价格最低的sku保存到索引库中
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(id).andStatusEqualTo("1").
                andIsDefaultEqualTo("1").andNumGreaterThan(0);
        List<Item> items = itemDao.selectByExample(itemQuery);
        if (items !=null && items.size()>0){
            //处理商品的规格
            for (Item item : items) {
                String spec = item.getSpec();
                Map<String,String> specMap = JSON.parseObject(spec, Map.class);
                item.setSpecMap(specMap);
            }
            //将数据保存到索引库中
            solrTemplate.saveBeans(items);
            solrTemplate.commit();
        }
    }

    //将数据库数据导入（tb_item)到索引库中
    private void dataImportToSolr() {
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andStatusEqualTo("1");
        List<Item> items = itemDao.selectByExample(itemQuery);
        if (items!=null && items.size()>0){
            //处理商品的规格
            for (Item item : items) {
                String spec = item.getSpec();
                Map<String,String> specMap = JSON.parseObject(spec, Map.class);
                item.setSpecMap(specMap);
            }
            //将数据保存到索引库中
            solrTemplate.saveBeans(items);
            solrTemplate.commit();
        }
    }

    /**
     * 删除商品（逻辑删除）
     * @param ids
     */
    @Transactional
    @Override
    public void delete(Long[] ids) {
        if (ids !=null && ids.length>0) {
            Goods goods = new Goods();
            goods.setIsDelete("1");
            for (final Long id : ids) {
                goods.setId(id);
                goodsDao.updateByPrimaryKeySelective(goods);
                //删除索引库中的的商品（下架）
//                SimpleQuery query=new SimpleQuery("item_goodsid:"+id);
//                solrTemplate.delete(query);
//                solrTemplate.commit();
                //将商品id发送到mq中
                jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        TextMessage textMessage = session.createTextMessage(String.valueOf(id));

                        return textMessage;
                    }
                });
            }
        }
    }

    /**
     * 设置item的公共的属性
     */
    private void setAttributeForItem(Item item,Goods goods,GoodsDesc goodsDesc){
        // 设置库存的图片：一张图片
        // 图片数据：在goods_desc中（该商品所有的图片）
        String itemImages = goodsDesc.getItemImages();
        List<Map> images = JSON.parseArray(itemImages, Map.class);
        if (images != null && images.size()>0){
            //只取一张图片
            String image = images.get(0).get("url").toString();
            item.setImage(image);
        }
        item.setCategoryid(goods.getCategory3Id());//设置三级分类id
        item.setStatus("1");//库存状态：1 ，正常
        item.setCreateTime(new Date());//创建日期
        item.setUpdateTime(new Date());//更新日期
        item.setSellerId(goods.getSellerId());//商家id
        item.setGoodsId(goods.getId()); // 商品id
        item.setCategory(itemCatDao.selectByPrimaryKey(goods.getCategory3Id()).getName());//设置分类名称
        item.setBrand(brandDao.selectByPrimaryKey(goods.getBrandId()).getName());//品牌名称
        item.setSeller(sellerDao.selectByPrimaryKey(goods.getSellerId()).getNickName());//商家店铺名称
    }

    public void updateMarketable(Long[] ids, String markeStatus) {
        if(ids != null && ids.length > 0){
            Goods goods = new Goods();
            goods.setIsMarketable(markeStatus);
            for (final Long id : ids) {
                goods.setId(id);
                // 1、更新商品的审核状态
                goodsDao.updateByPrimaryKeySelective(goods);
                // 如果审核通过
                if("1".equals(markeStatus)){
                    // 2、将商品进行上架
                    // 将所有的库存信息保存到索引库中
                    // 生成该商品详情的静态页面
                    // 需要将商品id发送到mq中
                    jmsTemplate.send(topicPageAndSolrDestination, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            // 将商品的id封装成消息体进行发送
                            TextMessage textMessage = session.createTextMessage(String.valueOf(id));
                            return textMessage;
                        }
                    });
                }else{
                    // 将商品进行下架：将商品信息从索引库中删除
                    // 删除静态页：可选(在本项目中不删除)
                    // 将商品的id发送到mq中
                    jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            // 将数据封装到消息体
                            TextMessage textMessage = session.createTextMessage(String.valueOf(id));
                            return textMessage;
                        }
                    });

                }
            }
        }
    }
}

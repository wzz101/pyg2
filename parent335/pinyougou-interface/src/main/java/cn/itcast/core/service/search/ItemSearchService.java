package cn.itcast.core.service.search;

import java.util.Map;

public interface ItemSearchService {
    /**
     * 前台系统的检索
     * @param searchMap
     * @return
     */
    public Map<String,Object> search(Map<String,String> searchMap);

    /**
     *商品上架
     */
    public void isShow(Long id);

    /**
     * 商品下架
     */
    public void deleteItemFromSolr(Long id);
}

package cn.itcast.core.controller.collect;

import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.service.search.ItemSearchService;
import cn.itcast.core.service.user.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/collect")
public class CollectController {

    @Reference
    private UserService userService;

    @Reference
    private ItemSearchService itemSearchService;

    /**
     * 查询用户收藏
     * @param
     * @return
     */
    @RequestMapping("/search.do")
    public List<Item> findCollectList() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
//        String name ="lijialong";
        System.out.println(name);
        List<Long> ids = userService.findById2(name);
        List<Item> byItemId = itemSearchService.findByItemId(ids);

        Item item = byItemId.get(0);
        System.out.println(item.getId());

        return byItemId;
    }


}

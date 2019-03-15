package cn.itcast.core.controller.temp;

import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.service.template.TypeTemplateService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {
    @Reference
    private TypeTemplateService typeTemplateService;

    /**
     * 新增商品：回显品牌以及扩展属性
     */
    @RequestMapping("/findOne.do")
    public TypeTemplate findOne(Long id){
        return typeTemplateService.findOne(id);
    }

    /**
     *新增商品：回显规格以及规格选项
     */
    @RequestMapping("/findBySpecList.do")
    public List<Map> findBySpecList(Long id){
    return typeTemplateService.findBySpecList(id);
    }
}

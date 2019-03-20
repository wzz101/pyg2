package cn.itcast.core.controller.temp;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.service.template.TypeTemplateService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {

    @Reference
    private TypeTemplateService typeTemplateService;

    /**
     * 模板列表查询
     * @param page
     * @param rows
     * @param typeTemplate
     * @return
     */
    @RequestMapping("/search.do")
    public PageResult search(Integer page, Integer rows, @RequestBody TypeTemplate typeTemplate){
        return typeTemplateService.search(page, rows, typeTemplate);
    }
    /**
     * 保存模板
     */

    @RequestMapping("/add.do")
    public Result add(@RequestBody TypeTemplate typeTemplate){
        try {
            typeTemplateService.add(typeTemplate);
            return new Result(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "保存失败");
        }
    }

    /*
     * 模板审核
     * */
    @RequestMapping("/updateStatus.do")
    public Result updateStatus(Long[] ids, String status) {
        try {
            typeTemplateService.updateStatus(ids, status);
            return new Result(true, "审核通过");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, "审核通过");
        }

    }
}

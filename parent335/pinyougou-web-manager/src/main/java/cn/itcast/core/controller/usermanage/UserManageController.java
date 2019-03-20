package cn.itcast.core.controller.usermanage;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.usermanage.UserManageService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserManageController {

    @Reference
    private UserManageService userManageService;

    /**
     * 运营商管理用户分页查询
     * @param pageSize
     * @return
     */
    @RequestMapping("/search.do")
    public PageResult search(Integer pageNo, Integer pageSize,@RequestBody User user){
        return  userManageService.search(pageNo,pageSize,user);
    }

    /**
     * 运营商冻结（解冻）用户
     * @param ids
     * @param status
     */
    @RequestMapping("/updateStatus.do")
    public Result updateStatus(Long[] ids,String status){
        try {
            userManageService.updateStatus(ids,status);
            return new Result(true,"冻结成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"冻结失败");

        }
    }

    /**
     * 运营商删除用户
     */
    @RequestMapping("/delete.do")
    public Result delete(Long[] ids){
        try {
            userManageService.delete(ids);
            return new Result(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除失败");
        }

    }
}

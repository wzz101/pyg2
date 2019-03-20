package cn.itcast.core.controller.user;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.user.UserService;
import cn.itcast.core.utils.checkphone.PhoneFormatCheckUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    private UserService userService;

    @RequestMapping("/sendCode.do")
    public Result sendCode(String phone){
        try {
            boolean phoneLegal = PhoneFormatCheckUtils.isPhoneLegal(phone);
            if(!phoneLegal){
                return new Result(false,"手机号不合法");
            }
            userService.sendCode(phone);
            return new Result(true,"短信发送成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true,"短信发送失败");
        }
    }
    /**
     * 用户注册
     * @param smscode
     * @param user
     * @return
     */
    @RequestMapping("/add.do")
    public Result add(String smscode, @RequestBody User user){
        try {
            userService.add(user, smscode);
            return new Result(true, "注册成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "注册失败");
        }
    }

    /**
     * 用户查询所有订单
     * @param page
     * @param rows
     * @param order
     * @return
     */
    @RequestMapping("/search.do")
    public PageResult searchUserOrde(Integer page, Integer rows, @RequestBody Order order) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
//        String userName = "lijialong";
        PageResult search = userService.search(userName, page, rows, order);
        return search;
    }


    /**
     * 回显用户信息
     * @param
     * @return
     */
    @RequestMapping("/findOneByuserName")
    public List<User> findOneByuserName() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        List<User> userList = userService.findOneByuserName(userName);
        return userList;
    }

    /**
     * 用户信息更新
     * @param user
     */
    @RequestMapping("/update.do")
    public Result update(@RequestBody User user){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            userService.upDate(user);
            return new Result(true, "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "更新失败");
        }

    }


}

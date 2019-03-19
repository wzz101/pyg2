package cn.itcast.core.controller.login;

import cn.itcast.core.service.user.UserService;
import cn.itcast.core.service.usermanage.UserManageService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

   @Reference
    private UserManageService userManageService;
    /**
     * 显示当前登录人
     */
    @RequestMapping("/name.do")
    public Map<String,String> name(){
        HashMap<String, String> map = new HashMap<>();
        String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
        map.put("loginName", loginName);
        //保存当前登录次数
        userManageService.userActive(loginName);
        return map;

    }


}

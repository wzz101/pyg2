package cn.itcast.core.service.usermanage;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.ad.Content;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.user.User;

public interface UserManageService {
    /**
     * 运营商管理用户

     * @param pageSize
     * @return
     */
    public PageResult search(Integer pageNo,Integer pageSize,User user);

    /**
     * 运营商冻结（解冻）用户
     * @param ids
     * @param status
     */
    void updateStatus(Long[] ids, String status);

    /**
     * 运营商批量删除用户
     * @param ids
     */
    void delete(Long[] ids);

    /**
     * 将用户登录次数储存在redis
     */
    public void userActive(String loginName);
}

package cn.itcast.core.service.address;

import cn.itcast.core.pojo.address.Address;

import java.util.List;

public interface AddressService {
    /**
     * 获取当前登录人的地址列表
     */
    public List<Address> findAddressList(String userId);
}

package cn.itcast.core.service.address;

import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.pojo.address.Areas;
import cn.itcast.core.pojo.address.Cities;
import cn.itcast.core.pojo.address.Provinces;

import java.util.List;

public interface AddressService {
    /**
     * 获取当前登录人的地址列表
     */
    public List<Address> findAddressList(String userId);

    /**
     * c
     *
     */
    public List<Address> findAddressListByLoginUser(String userName);

    Address findOne(Long id);

    void add(Address address);

    void update(Address address);

    void delete(Long id);

    public void setDefaultAddress(Long id);

    public List<Provinces> findProvinceByparentId(String parentId);

    public List<Cities> findCityByparentId(String parentId);

    public List<Areas> findAreaByparentId(String parentId);
//
}

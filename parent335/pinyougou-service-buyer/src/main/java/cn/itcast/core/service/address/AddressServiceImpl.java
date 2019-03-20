package cn.itcast.core.service.address;

import cn.itcast.core.dao.address.AddressDao;
import cn.itcast.core.dao.address.AreasDao;
import cn.itcast.core.dao.address.CitiesDao;
import cn.itcast.core.dao.address.ProvincesDao;
import cn.itcast.core.pojo.address.*;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{
    @Resource
    private AddressDao addressDao;


    @Resource
    private ProvincesDao provincesDao;
    @Resource
    private CitiesDao citiesDao;
    @Resource
    private AreasDao areasDao;

    /**
     * 获取当前登录人的地址列表
     * @param userId
     * @return
     */
    @Override
    public List<Address> findAddressList(String userId) {
        AddressQuery addressQuery = new AddressQuery();
        addressQuery.createCriteria().andUserIdEqualTo(userId);
        return addressDao.selectByExample(addressQuery);
    }

    /**
     * 用户获取自己的地址
     * @param userName
     * @return
     */
    @Override
    public List<Address> findAddressListByLoginUser(String userName) {
        AddressQuery addressQuery = new AddressQuery();
        AddressQuery.Criteria criteria = addressQuery.createCriteria();
        criteria.andUserIdEqualTo(userName);
        List<Address> addressList = addressDao.selectByExample(addressQuery);
        return addressList;
    }


    /**
     * 回显地址
     * @param id
     * @return
     */
    @Override
    public Address findOne(Long id) {
        return addressDao.selectByPrimaryKey(id);
    }

    /**
     * 添加地址
     * @param address
     */
    @Override
    @Transactional
    public void add(Address address) {
        addressDao.insertSelective(address);
    }

    /**
     * 修改地址
     * @param address
     */
    @Transactional
    @Override
    public void update(Address address) {
        Long id = address.getId();
        addressDao.updateByPrimaryKeySelective(address);

    }

    /**
     * 删除地址
     * @param id
     */
    @Transactional
    @Override
    public void delete(Long id) {
        addressDao.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional
    public void setDefaultAddress(Long id) {
        /**
         * 将原来的默认地址设为非默认
         */
        Address address=new Address();
        address.setIsDefault("0");
        AddressQuery query = new AddressQuery();
        AddressQuery.Criteria criteria = query.createCriteria();
        criteria.andIsDefaultEqualTo("1");
        addressDao.updateByExampleSelective(address,query);
        /**
         * 将页面传过来的地址id设为默认地址
         */
        Address address1 = new Address();
        address1.setId(id);
        address1.setIsDefault("1");
        addressDao.updateByPrimaryKeySelective(address1);
    }


    @Override
    public List<Provinces> findProvinceByparentId(String parentId) {
        List<Provinces> provinces = provincesDao.selectByExample(null);
        return provinces;
    }

    @Override
    public List<Cities> findCityByparentId(String parentId) {
        CitiesQuery query = new CitiesQuery();
        CitiesQuery.Criteria criteria = query.createCriteria();
        criteria.andProvinceidEqualTo(parentId);
        return citiesDao.selectByExample(query);
    }

    @Override
    public List<Areas> findAreaByparentId(String parentId) {
        AreasQuery query=new AreasQuery();
        AreasQuery.Criteria criteria = query.createCriteria();
        criteria.andCityidEqualTo(parentId);
        return areasDao.selectByExample(query);
    }
}

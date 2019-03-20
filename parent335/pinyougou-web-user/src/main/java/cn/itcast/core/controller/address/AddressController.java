package cn.itcast.core.controller.address;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.pojo.address.Areas;
import cn.itcast.core.pojo.address.Cities;
import cn.itcast.core.pojo.address.Provinces;
import cn.itcast.core.service.address.AddressService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Reference
    private AddressService addressService;

    /**
     * 获取当前登录人的地址列表
     * @param
     * @return
     */
    @RequestMapping("/findListByLoginUser.do")
    public List<Address> findListByLoginUser(){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
//        String userName ="lijialong";
       return addressService.findAddressListByLoginUser(userName);
    }

    /**
     * 回显地址
     * @param id
     * @return
     */
    @RequestMapping("/findOne.do")
    public Address findOne(Long id){
        return addressService.findOne(id);
    }

    /**
     * 添加地址
     * @param address
     * @return
     */
    @RequestMapping("/add.do")
    public Result add(@RequestBody Address address){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        address.setUserId(userName);
        try {
            addressService.add(address);
            return new Result(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "保存失败");
        }

    }

    /**
     * 修改
     * @param address
     * @return
     */
    @RequestMapping("/update.do")
    public Result update(@RequestBody Address address){
        try {
            addressService.update(address);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }

    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping("/delete.do")
    public Result delete(Long id){
        try {
            addressService.delete(id);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }

    }

    /**
     * 修改默认地址
     * @param id
     * @return
     */
    @RequestMapping("/setDefaultAddress")
    public Result setDefaultAddress(Long id){
        try {
            addressService.setDefaultAddress(id);
            return new Result(true, "修改默认地址成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, "修改默认地址失败");
        }
    }

    /**
     * 查询省份
     * @return
     */
    @RequestMapping("/findProvince")
    public List<Provinces> findProvinceByparentId(String parentId) {
        List<Provinces> ProvincesList = addressService.findProvinceByparentId(parentId);
        return ProvincesList;
    }

    /**
     * 根据父级ID查询城市
     * @return
     */
    @RequestMapping("/findCity")
    public List<Cities> findCityByparentId(String parentId) {
        List<Cities> CityList = addressService.findCityByparentId(parentId);
        return CityList;
    }

    /**
     * 根据父级ID查询区域
     * @return
     */
    @RequestMapping("/findArea")
    public List<Areas> findAreaByparentId(String parentId) {
        List<Areas> AreaList = addressService.findAreaByparentId(parentId);
        return AreaList;
    }

}

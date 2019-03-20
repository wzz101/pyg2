package cn.itcast.core.controller.brand;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.service.brand.BrandService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    /**
     * 查询所有的品牌
     * @return
     */
    @RequestMapping("/findAll.do")
    public List<Brand> findAll(){
        return brandService.findAll();
    }
    /**
     * 分页查询所有的品牌
     */
    @RequestMapping("/findPage.do")
    public PageResult findPage(Integer pageNo, Integer pageSize){
        return brandService.findPage(pageNo,pageSize);
    }

    @RequestMapping("/search.do")
    public PageResult search(Integer pageNo, Integer pageSize,@RequestBody Brand brand){
       return  brandService.search(pageNo,pageSize,brand);
    }

    /**
     * 保存
     * @param brand
     * @return
     */
    @RequestMapping("/add.do")
    public Result add(@RequestBody Brand brand){
        try {
            brandService.add(brand);
            return new Result(true,"保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"保存失败");
        }
    }

    /**
     * 回显品牌
     * @return
     */
    @RequestMapping("/findOne.do")
    public Brand findOne(Long id){
        return brandService.findOne(id);
    }

    /**
     * 更新品牌
     */
    @RequestMapping("/update.do")
    public Result update(@RequestBody Brand brand){
        try {
            brandService.update(brand);
            return new Result(true,"更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"更新失败");
        }
    }

    /**
     * 批量删除
     */
    @RequestMapping("/delete.do")
    public Result delete(Long[] ids){
        try {
            brandService.delete(ids);
            return new Result(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除失败");
        }
    }

    /**
     * 新增模板时需要加载的品牌结果集
     * @return
     */
    @RequestMapping("/selectOptionList.do")
    public List<Map> selectOptionList(){

        return brandService.selectOptionList();
    }

    /*
     * 审核品牌
     * */
    @RequestMapping("/updateStatus.do")
    public Result updateStatus(Long[] ids, String status) {

        try {
            brandService.updateStatus(ids, status);
            return new Result(true, "审核成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "审核失败");
        }
    }
    @RequestMapping("/importExcel.do")
    public Result importExcel(){
        try {
            String filePath = "C:\\Users\\juchen\\Desktop\\1.xls";
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setTitleRows(1);
            List<Brand> list = brandService.findAll();

            List<Brand> brandList = ExcelImportUtil.importExcel(new File(filePath), Brand.class, params);
            for (Brand brand : brandList) {
                brandService.add(brand);
                System.out.println(brand.getName());
            }
            return new Result(true,"上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"上传失败");
        }


    }

}

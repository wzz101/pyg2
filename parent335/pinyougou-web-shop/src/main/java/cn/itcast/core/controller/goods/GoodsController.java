package cn.itcast.core.controller.goods;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.service.goods.GoodsService;
import cn.itcast.core.vo.GoodsVo;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Reference
    private GoodsService goodsService;

    /**
     * 商品保存
     */
    @RequestMapping("/add.do")
    public Result add(@RequestBody GoodsVo goodsVo){
        try {
            //设置商家id
            String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
            goodsVo.getGoods().setSellerId(sellerId);
            goodsService.add(goodsVo);
            return new Result(true,"保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"保存失败");
        }
    }

    /**
     * 商家系统下的列表查询
     */
    @RequestMapping("/search.do")
    public PageResult search(Integer page, Integer rows,@RequestBody Goods goods){
        //设置商家id
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        goods.setSellerId(sellerId);
        return goodsService.search(page,rows,goods);
    }

    /**
     * 商品回显
     */
    @RequestMapping("/findOne.do")
    public GoodsVo findOne(Long id){
        return goodsService.findOne(id);
    }

    /**
     * 商品更新
     */
    @RequestMapping("/update.do")
    public Result update(@RequestBody GoodsVo goodsVo){
        try {
            goodsService.update(goodsVo);
            return new Result(true,"更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"更新失败");
        }
    }
    @RequestMapping("/updateMarketable.do")
    public Result updateMarketable(Long[] ids, String markeStatus){
        try {
            goodsService.updateMarketable(ids,markeStatus);
            return new Result(true, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "操作失败");
        }
    }
    @RequestMapping("/downExcel.do")
    public void downExcel(HttpServletResponse response){
        try {
            List<Goods> goodsList = goodsService.findAll();
            ExportParams params = new ExportParams("商品表", "商品");
            String fileName = "商品表";
            Workbook workbook = ExcelExportUtil.exportExcel(params, Goods.class, goodsList);

            response.setHeader("content-Type","application/vnd.ms-excel");

            response.setHeader("Content-Disposition", "attachment;filename="
                    + new String(fileName.getBytes(),"iso-8859-1") + ".xls");
            response.setCharacterEncoding("UTF-8");
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package cn.itcast.core.controller.order;


import cn.itcast.core.entity.LineEcharts;
import cn.itcast.core.entity.PieDataEcharts;
import cn.itcast.core.entity.PieEcharts;
import cn.itcast.core.service.order.EchartsService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Reference
    private EchartsService echartService;

    @RequestMapping("/line.do")
    @ResponseBody
    public Map<String, LineEcharts> line(HttpServletRequest request){
        String time = request.getParameter("time");
        Map<String, LineEcharts> yysline = echartService.yysline(time);
        return yysline;
    }

    @RequestMapping("/pie.do")
    @ResponseBody
    public PieEcharts pie(HttpServletRequest request){
        String time = request.getParameter("time");
        List<PieDataEcharts> yyspie = echartService.yyspie(time);
        PieEcharts pieEcharts = new PieEcharts();
        pieEcharts.setName("金额占比");
        pieEcharts.setType("pie");
        pieEcharts.setRadius("55%");
        pieEcharts.setData(yyspie);
        return pieEcharts;
    }


}

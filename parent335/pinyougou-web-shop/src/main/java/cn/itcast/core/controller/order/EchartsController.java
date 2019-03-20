package cn.itcast.core.controller.order;

import cn.itcast.core.entity.LineEcharts;
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
    public Map<String, LineEcharts> line(HttpServletRequest request ){
        String time = request.getParameter("time");
        String id = request.getParameter("id");
        Map<String, LineEcharts> yysline = echartService.sjline(time,id);
        return yysline;
    }




}

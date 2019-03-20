package cn.itcast.core.service.order;



import cn.itcast.core.entity.LineEcharts;
import cn.itcast.core.entity.PieDataEcharts;

import java.util.List;
import java.util.Map;

public interface EchartsService {

    Map<String, LineEcharts> yysline(String time);

    public List<PieDataEcharts> yyspie(String time);

    public Map<String, LineEcharts> sjline(String time, String id);
}

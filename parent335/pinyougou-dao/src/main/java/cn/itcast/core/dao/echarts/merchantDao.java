package cn.itcast.core.dao.echarts;

import java.util.List;

public interface merchantDao {

    //商家订单
    List<String> sjMonthlySales(String time, String seller_id);



}

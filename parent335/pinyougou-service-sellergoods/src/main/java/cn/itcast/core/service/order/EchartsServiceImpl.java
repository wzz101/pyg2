package cn.itcast.core.service.order;


import cn.itcast.core.dao.echarts.operaterDao;
import cn.itcast.core.dao.echarts.merchantDao;
import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.entity.LineEcharts;
import cn.itcast.core.entity.PieDataEcharts;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.order.OrderItem;
import com.alibaba.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EchartsServiceImpl implements EchartsService {

    @Resource
    private operaterDao operaterDao;
    @Resource
    private merchantDao merchantDao;

    @Resource
    private OrderItemDao orderItemDao;

    @Resource
    private GoodsDao goodsDao;

    @Override
    public Map<String, LineEcharts> yysline(String time) {

        Map<String, LineEcharts> map = new HashMap<>();
        LineEcharts echarts = new LineEcharts();
        echarts.setName("销量");
        echarts.setType("line");
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            String s = "%" + time + "%";
            if (i < 10) {
                s = s + "0" + i;
            } else {
                s = s + i;
            }
            int i1 = operaterDao.yysMonthlySales(s);
            list.add(i1);
        }
        echarts.setData(list);
        map.put("yysdata", echarts);
        return map;
    }

    @Override
    public List<PieDataEcharts> yyspie(String time) {

        List<PieDataEcharts> list = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            PieDataEcharts pieEcharts = new PieDataEcharts();
            String s = "%" + time + "%";
            if (i < 10) {
                s = s + "0" + i;
            } else {
                s = s + i;
            }
            if (operaterDao.yysMoneyNull(s) != 0) {
                double v = operaterDao.yysMonthlySalesMoney(s);
                pieEcharts.setName(i + "月");
                pieEcharts.setValue(v);
                list.add(pieEcharts);

            } else {
                pieEcharts.setName(i + "月");
                pieEcharts.setValue(0.0);
                list.add(pieEcharts);
            }
        }
        return list;
    }

    @Override
    public Map<String, LineEcharts> sjline(String time, String id) {
        Map<String, LineEcharts> map = new HashMap<>();
        Map<String, Integer> ccmap = new HashMap<>();
        Map<String, List<Integer>> listmap = new HashMap<>();

        List<String> spid = goodsDao.sjspid(id);
        for (String s : spid) {
            ArrayList<Integer> list = new ArrayList<>();
            ccmap.put(s, 0);
            listmap.put(s, list);
        }
        //这个月的所有的订单号
        for (int i = 1; i <= 12; i++) {
            Map<String, Integer> cmap = new HashMap<>();
            cmap.putAll(ccmap);
            String s = "%" + time + "%";
            if (i < 10) {
                s = s + "0" + i;
            } else {
                s = s + i;
            }
            List<String> ddlist = merchantDao.sjMonthlySales(s, id);
            if (ddlist.size() == 0) {
                for (Map.Entry<String, List<Integer>> m : listmap.entrySet()) {
                    List<Integer> d1list = m.getValue();
                    d1list.add(0);
                    listmap.put(m.getKey(), d1list);
                }
            } else {
                for (String d1 : ddlist) {
                    List<OrderItem> orderItemList = orderItemDao.sjInformation(d1, id);
                    for (OrderItem orderItem : orderItemList) {
                        Integer num1 = orderItem.getNum();
                        Integer i2 = cmap.get(orderItem.getGoodsId().toString());
                        cmap.put(orderItem.getGoodsId().toString(), (num1 + i2));
                    }
                }
                for (Map.Entry<String, List<Integer>> lmap : listmap.entrySet()) {
                    Goods goods = goodsDao.selectByPrimaryKey(Long.parseLong(lmap.getKey()));
                    if (listmap.containsKey(lmap.getKey())) {
                        List<Integer> list = lmap.getValue();
                        String key = lmap.getKey();
                        Integer i0 = cmap.get(lmap.getKey());
                        list.add(cmap.get(lmap.getKey()));
                        listmap.put(lmap.getKey(), list);
                    }
                }
            }
        }

        for (Map.Entry<String, List<Integer>> entry : listmap.entrySet()) {
            Goods goods = goodsDao.selectByPrimaryKey(Long.parseLong(entry.getKey()));
            LineEcharts echarts = new LineEcharts();
            echarts.setName(goods.getGoodsName());
            echarts.setType("line");
            echarts.setData(entry.getValue());
            map.put(entry.getKey(), echarts);
        }
        return map;

    }

}

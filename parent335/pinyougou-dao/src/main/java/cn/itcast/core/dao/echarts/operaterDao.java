package cn.itcast.core.dao.echarts;

public interface operaterDao {

    //运营商折线图
    int yysMonthlySales(String time);

    double yysMonthlySalesMoney(String time);

    int yysMoneyNull(String time);


}

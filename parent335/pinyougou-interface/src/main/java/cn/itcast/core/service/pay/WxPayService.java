package cn.itcast.core.service.pay;

import java.util.Map;

public interface WxPayService {
    /**
     * 需求：提交订单，跳转支付页面，生成二维码 （需要向后台发送请求，获取二维码支付地址）
     * 参数：userId
     * 返回值：Map (支付地址。支付金额，支付是否成功)
     */
    public Map createNative(String userId);

    /**
     * 需求：根据订单号（交易流水号）查询订单支付状态 （支付成功，失败，关闭，未支付等等）
     * 参数：String out_trade_no
     * 返回值：map
     */
    public Map queryPayStatus(String out_trade_no);
}

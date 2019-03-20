// 定义服务层:
app.service("collectService",function($http) {

    //购物车列表
    this.search=function(){
        return $http.get('../collect/search.do');
    }
    // //添加商品到购物车
    // this.addGoodsToCartList=function(itemId,num){
    // 	return $http.get('cart/addGoodsToCartList.do?itemId='+itemId+'&num='+num);
    // }
    //
    // //求合计数
    // this.sum=function(collectList){
    // 	var totalValue={totalNum:0,totalMoney:0 };
    //
    // 	for(var i=0;i<collectList.length ;i++){
    // 		var collect=collectList[i];//购物车对象
    // 		for(var j=0;j<collect.orderItemList.length;j++){
    // 			var orderItem=  cart.orderItemList[j];//购物车明细
    // 			totalValue.totalNum+=orderItem.num;//累加数量
    // 			totalValue.totalMoney+=orderItem.totalFee;//累加金额
    // 		}
    // 	}
    // 	return totalValue;

    // }
    //
    // //获取当前登录账号的收货地址
    // this.findAddressList=function(){
    // 	return $http.get('address/findListByLoginUser.do');
    // }
    //
    // //提交订单
    // this.submitOrder=function(order){
    // 	return $http.post('order/add.do',order);
    // }
    //
})
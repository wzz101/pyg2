// 地址服务层
app.service('addressService', function($http) {

    // 获取地址列表
    this.findAddressList = function(){
        return $http.get('address/findListByLoginUser.do');
    };
    // 增加
    this.add = function(address){
        return $http.post('address/add.do/', address);
    };
    // 修改
    this.update = function(address){
        return $http.post('address/update.do/', address);
    };
    this.findOne = function (id) {
        return $http.get('address/findOne.do?id='+id);
    };
    this.del = function (id) {
        return $http.get('address/delete.do?id='+id);
    };

    //设置默认地址
    this.setDefaultAddress = function (id) {
        return $http.get('address/setDefaultAddress.do?id=' + id);
    }

    this.findProvince = function(parentId){
        return $http.get("../address/findProvince.do?parentId="+parentId);
    }
    this.findCity = function(parentId){
        return $http.get("../address/findCity.do?parentId="+parentId);
    }
    this.findArea = function(parentId){
        return $http.get("../address/findArea.do?parentId="+parentId);
    }

});

// 定义服务层:
app.service("userService",function($http){

    //分页条件查询
    this.search = function(page,rows,searchEntity){
        return $http.post("../user/search.do?pageNo="+page+"&pageSize="+rows,searchEntity);
    }

    //修改状态的方法
    this.updateStatus = function(ids,status){
        return $http.get('../user/updateStatus.do?ids='+ids+"&status="+status);
    }

    //删除
    this.dele=function(ids){
        return $http.get('../user/delete.do?ids='+ids);
    }
});
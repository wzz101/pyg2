//控制层
app.controller('userController',function($scope,$controller,userService){

    $controller('baseController',{$scope:$scope});//继承

    // 假设定义一个查询的实体：searchEntity
    $scope.searchEntity={};

    $scope.search = function(page,rows){
        // 向后台发送请求获取数据:
        userService.search(page,rows,$scope.searchEntity).success(function(response){
            $scope.paginationConf.totalItems = response.total;
            $scope.list = response.rows;
        });
    }
    $scope.search1 = function(page,rows){
        $scope.searchEntity={status:'1'};
        // 向后台发送请求获取数据:
        userService.search(page,rows,$scope.searchEntity).success(function(response){
            $scope.paginationConf.totalItems = response.total;
            $scope.list = response.rows;
        });
    }
    $scope.search0 = function(page,rows){
        $scope.searchEntity={status:'0'};
        // 向后台发送请求获取数据:
        userService.search(page,rows,$scope.searchEntity).success(function(response){
            $scope.paginationConf.totalItems = response.total;
            $scope.list = response.rows;
        });
    }
    $scope.search3A = function(page,rows){
        $scope.searchEntity={activeCount:'3'};
        // 向后台发送请求获取数据:
        userService.search(page,rows,$scope.searchEntity).success(function(response){
            $scope.paginationConf.totalItems = response.total;
            $scope.list = response.rows;
        });
    }
    $scope.search10A = function(page,rows){
        $scope.searchEntity={activeCount:'10'};
        // 向后台发送请求获取数据:
        userService.search(page,rows,$scope.searchEntity).success(function(response){
            $scope.paginationConf.totalItems = response.total;
            $scope.list = response.rows;
        });
    }
    $scope.status = ["冻结","正常"];

    // 修改状态的方法:
    $scope.updateStatus = function(status){
        userService.updateStatus($scope.selectIds,status).success(function(response){
            if(response.flag){
                $scope.reloadList();//刷新列表
                $scope.selectIds = [];
            }else{
                alert(response.message);
            }
        });
    }


    //批量删除
    $scope.dele=function(){
        //获取选中的复选框
        userService.dele( $scope.selectIds ).success(
            function(response){
                if(response.flag){
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }
});
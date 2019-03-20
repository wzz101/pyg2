app.controller('indexController',function($scope,$controller,loginService,userService){
    $controller('baseController', {$scope: $scope}); // 继承
	$scope.showName=function(){
			loginService.showName().success(
					function(response){
						$scope.loginName=response.loginName;
					}
			);
	}

    $scope.findAll=function(){
        userService.findAll().success(
            function(response){
                $scope.list=response;
            }
        );
    }
    $scope.prePay=function(status){
        $scope.searchEntity = {status:status}
        this.search(1, 7);
    }

    $scope.searchEntity={};

    // 假设定义一个查询的实体：searchEntity
    $scope.search = function(page,rows){
        // 向后台发送请求获取数据:
        userService.search(page,rows,$scope.searchEntity).success(function(response){
            $scope.paginationConf.totalItems = response.total;
            $scope.list = response.rows;
        });
    }



});
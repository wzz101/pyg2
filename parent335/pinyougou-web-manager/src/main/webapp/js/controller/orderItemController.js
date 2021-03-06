//控制层
app.controller('orderItemController' ,function($scope,$controller  ,orderItemService){

        $controller('baseController', {$scope: $scope});//继承


        //读取列表数据绑定到表单中
        $scope.findAll = function () {
            orderItemService.findAll().success(
                function (response) {
                    $scope.list = response;
                }
            );
        }

        //分页
        $scope.findPage = function (page, rows) {
            orderItemService.findPage(page, rows).success(
                function (response) {
                    $scope.list = response.rows;
                    $scope.paginationConf.totalItems = response.total;//更新总记录数
                }
            );
        }

        //查询实体
        $scope.findOne = function (id) {
            orderItemService.findOne(id).success(
                function (response) {
                    $scope.entity = response;
                }
            );
        }

        //保存
        $scope.save = function () {
            var serviceObject;//服务层对象
            if ($scope.entity.id != null) {//如果有ID
                serviceObject = orderItemService.update($scope.entity); //修改
            } else {
                serviceObject = orderItemService.add($scope.entity);//增加
            }
            serviceObject.success(
                function (response) {
                    if (response.flag) {
                        //重新查询
                        $scope.reloadList();//重新加载
                    } else {
                        alert(response.message);
                    }
                }
            );
        }


        //批量删除
        $scope.dele = function () {
            //获取选中的复选框
            orderItemService.dele($scope.selectIds).success(
                function (response) {
                    if (response.flag) {
                        $scope.reloadList();//刷新列表
                        $scope.selectIds = [];
                    }
                }
            );
        }

        $scope.searchEntity = {};//定义搜索对象

        //搜索
        $scope.search = function (page, rows) {
            orderItemService.search(page, rows, $scope.searchEntity).success(
                function (response) {
                    $scope.list = response.rows;
                    $scope.paginationConf.totalItems = response.total;//更新总记录数
                }
            );
        }

        // 显示状态
        $scope.status = ["未付款", "已付款", "未发货", "已发货", "交易成功", "交易关闭", "待评价"];


        审核的方法:
            $scope.updateStatus = function (status) {
                orderItemService.updateStatus($scope.selectIds, status).success(function (response) {
                    if (response.flag) {
                        $scope.reloadList();//刷新列表
                        $scope.selectIds = [];
                    } else {
                        alert(response.message);
                    }
                });
            }


});

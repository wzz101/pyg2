app.controller('userController' ,function($scope,$controller ,userService,loginService,uploadService) {
    $controller('baseController', {$scope: $scope}); // 继承

    $scope.showName = function () {
        loginService.showName().success(
            function (response) {
                $scope.loginName = response.loginName;
            }
        );
    }

    $scope.entity = {};
    //注册用户
    $scope.reg = function () {

        //比较两次输入的密码是否一致
        if ($scope.password != $scope.entity.password) {
            alert("两次输入密码不一致，请重新输入");
            $scope.entity.password = "";
            $scope.password = "";
            return;
        }
        //新增
        userService.add($scope.entity, $scope.smscode).success(
            function (response) {
                alert(response.message);
            }
        );
    }

    //发送验证码
    $scope.sendCode = function () {
        if ($scope.entity.phone == null || $scope.entity.phone == "") {
            alert("请填写手机号码");
            return;
        }

        userService.sendCode($scope.entity.phone).success(
            function (response) {
                alert(response.message);
            }
        );
    }
    // 文件上传
    $scope.uploadFile = function () {
        // 调用uploadService的方法完成文件的上传
        uploadService.uploadFile().success(function (response) {
            if (response.success) {

                alert(response.message);
                // $scope.image_entity.url =  response.message;
            } else {
                alert(response.message);
            }
        });
    }

    //显示当前用户名
    $scope.findOneByuserName = function () {

        userService.findOneByuserName().success(
            function (response) {
                $scope.userList = response;
            }
        );
    }

    //个人中心的用户注册
    $scope.regis=function(){

        //比较两次输入的密码是否一致
        if($scope.password!=$scope.entity.password){
            alert("两次输入密码不一致，请重新输入");
            $scope.entity.password="";
            $scope.password="";
            return ;
        }
        //新增
        userService.addUser($scope.entity,$scope.smscode).success(

            function(response){
                alert(response.message);

            }
        );
    }
    // 获得了image_entity的实体的数据{"color":"褐色","url":"http://192.168.209.132/group1/M00/00/00/wKjRhFn1bH2AZAatAACXQA462ec665.jpg"}
    $scope.entity={goods:{},goodsDesc:{itemImages:[],specificationItems:[]}};

    $scope.add_image_entity = function(){
        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    }

    $scope.remove_iamge_entity = function(index){
        $scope.entity.goodsDesc.itemImages.splice(index,1);
    }

})


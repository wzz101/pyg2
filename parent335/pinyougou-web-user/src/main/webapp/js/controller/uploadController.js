app.controller("uploadController",function($scope,uploadService){
    $scope.uploadFile = function(){
        // 调用uploadService的方法完成文件的上传
        uploadService.uploadFile().success(function(response){
            if(response.success){
                // 获得url
                alert(response.message);
                $scope.image_entity.url =  response.message;
            }else{
                alert(response.message);
            }
        });
    }
});
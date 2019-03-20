app.service('uploadService', function ($http) {
    this.uploadFile = function () {
        var formData = new FormData();
        formData.append("file", file.files[0]);
        return $http({
            method:'post',
            url:'../upload',
            data:formData,
            headers: {'Content-Type':undefined},
            transformRequest: angular.identity
        });
    }
    this.saveExcel = function(){
        // 向后台传递数据:
        var formData = new FormData();
        // 向formData中添加数据:
        formData.append("file",file.files[0]);

        return $http({
            method:'post',
            url:'../upload/saveExcel.do',
            data:formData,
            headers:{'Content-Type':undefined} ,// Content-Type : text/html  text/plain
            transformRequest: angular.identity
        });
    }

});
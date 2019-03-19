//服务层
app.service('userService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../usermanage/findAll.do');
	}
	//分页 
	this.findPage=function(page,rows){
		return $http.get('../usermanage/findPage.do?page='+page+'&rows='+rows);
	}
	//查询实体
	this.findOne=function(id){
		return $http.get('../usermanage/findOne.do?id='+id);
	}
	//增加 
	this.add=function(entity,smscode){
		return  $http.post('../usermanage/add.do?smscode='+smscode,entity );
	}
	//修改 
	this.update=function(entity){
		return  $http.post('../usermanage/update.do',entity );
	}
	//删除
	this.dele=function(ids){
		return $http.get('../usermanage/delete.do?ids='+ids);
	}
	//搜索
	this.search=function(page,rows,searchEntity){
		return $http.post('../usermanage/search.do?page='+page+"&rows="+rows, searchEntity);
	}
	//发送验证码
	this.sendCode=function(phone){
		return $http.get('../usermanage/sendCode.do?phone='+phone);
	}
	
});

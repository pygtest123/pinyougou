/** 定义控制器层 */
app.controller('personController', function($scope,baseService){


    /** 初始化查询所有省份 */
    $scope.findProvinces = function () {
        baseService.sendGet('/user/findProvinces').then(function (response) {
            $scope.provinceList = response.data;
        })
    };

    //定义个人信息数组封装个人数据
    $scope.user={address:{}};

    /** 封装职业数据 */
    $scope.saveProfession =function (profession) {
            $scope.user.profession = profession;
    };

    /** 封装区级数据 */
    $scope.saveArea =function () {
        $scope.user.address.area = $scope.areas.area;
    };

    /**　监控省级下拉列表根据provinceId选择查询城市名称　*/
    $scope.$watch('p.provinceId', function (newVal, oldVal) {
        //alert("新值：" + newVal + ",旧值:" + oldVal);
        if (newVal){ // 不是undefined、null
            // 查询二级分类
            $scope.findCityByParentId(newVal.provinceId,"cityList");
            $scope.user.address.province = newVal.province;
        }else {
            $scope.cityList = [];
        }
    });

    // 根据父级parentId查询,得到分类
    $scope.findCityByParentId = function (parentId, name) {
        baseService.sendGet("/user/findCityByParentId?parentId="
            +　parentId ).then(
                function (response) {
            // 获取响应数据
            $scope[name] = response.data;
        });
    };

    /**　监控城市下拉列表根据cityId选择查询区级名称　*/
    $scope.$watch('c.cityId', function (newVal, oldVal) {
        //alert("新值：" + newVal + ",旧值:" + oldVal);
        if (newVal){ // 不是undefined、null
            // 查询二级分类
            $scope.findAreaByCityId(newVal.cityId,"areaList");
            $scope.user.address.city =newVal.city;
        }else {
            $scope.areaList = [];

        }
    });

    // 根据父级cityId查询,得到分类
    $scope.findAreaByCityId = function (cityId, name) {
        baseService.sendGet("/user/findAreaByCityId?cityId=" + cityId).then(
            function (response) {
                // 获取响应数据
                $scope[name] = response.data;
            });
    };

    /**　封装性别数据　*/
    $scope.user.sex = '女';
    $scope.checkSex = function(sex){
            $scope.user.sex = sex;
    };

    /** 保存按钮点击事件,保存个人信息 */
    $scope.savePersonToUser = function () {
        baseService.sendPost('/user/savePersonToUser' ,$scope.user).then(function (response) {
            if(response.data){
                //保存成功,跳转首页
                location.href="http://user.pinyougou.com";
            }else {
                alert("完善信息失败,请重试!");
            }
        });
    };

    /** 定义文件上传 */
    $scope.uploadFile = function () {
        // 调用服务层上传文件
        baseService.uploadFile().then(function(response){
            // 获取响应数据 {status : 200, url : 'http://192.168.12.131/group1/xxx/xx/x.jpg'}
            if(response.data.status == 200){
                // 获取图片url $scope.picEntity : {url : '', color:''}
                $scope.user.headPic = response.data.url;
            }else{
                alert("上传失败！");
            }
        });
    };

});
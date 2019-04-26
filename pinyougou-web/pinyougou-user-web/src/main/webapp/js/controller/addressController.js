/** 定义控制器层 */
app.controller('addressController', function($scope,baseService){

    /** 初始化查询登录用户的所有地址 */
    $scope.findAll = function () {
      baseService.sendGet('/address/findAll').then(function (response) {
          $scope.addressList = response.data;
      })
    };

    //定义地址对象封装数据
    $scope.newAddress={};

    /** 点击新增地址查询所有省份 */
    $scope.findProvinces = function () {
        baseService.sendGet('/user/findProvinces').then(function (response) {
            $scope.provinceList = response.data;
        })
    };

    /**　监控省级下拉列表根据provinceId选择查询城市名称　*/
    $scope.$watch('p.provinceId', function (newVal, oldVal) {
        //alert("新值：" + newVal + ",旧值:" + oldVal);
        if (newVal){ // 不是undefined、null
            // 查询二级分类
            $scope.findCityByParentId(newVal.provinceId,"cityList");
            //封装省份ID数据
            $scope.newAddress.provinceId = newVal.provinceId;
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
            //封装城市ID
            $scope.newAddress.cityId =newVal.cityId;
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

    /** 封装区级数据ID */
    $scope.saveArea =function () {
        $scope.newAddress.townId = $scope.areas.areaId;
    };

        /** 新增用户地址 */
    $scope.saveAddress = function () {
          baseService.sendPost('/address/saveAddress', $scope.newAddress).then(
              function (response) {
              if(response.data){
                  //清空
                  $scope.newAddress ={};
                  //重新查询地址
                  $scope.findAll();
              }else {
                  alert("成功失败!")
              }
          })
        };

    /** 修改默认地址(修改isDefault状态码) */
    $scope.updateDefaultStatus =function (id) {
        baseService.sendGet('/address/updateDefaultStatus?id=' + id).then(function (response) {
            if(response.data){
                //重新查询地址
                $scope.findAll();
            }else {
                alert("设置失败!")
            }
        });
    };

    /** 删除选中的地址 */
    $scope.deleteAddress = function (id) {
      baseService.sendGet('/address/deleteAddress?id=' + id).then(function (response) {
          if(response.data){
              //重新查询地址
              $scope.findAll();
          }else {
              alert("删除失败!")
          }
      })
    };

});
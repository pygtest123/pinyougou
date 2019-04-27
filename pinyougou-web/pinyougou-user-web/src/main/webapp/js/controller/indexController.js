/** 定义控制器层 */
app.controller('indexController', function($scope, baseService){
    /** 定义搜索参数对象 */
    $scope.OrderParam = {page : 1, rows : 5};


    /** 定义获取登录用户名方法 */
    $scope.showName = function(){
        baseService.sendGet("/user/showName")
            .then(function(response){
                $scope.loginName = response.data.loginName;
            });
    };

    /** 定义订单对象 */
    $scope.OrderEntity = {};
    /** 分页查询订单信息 */
    $scope.search = function(page, rows){
        /** 调用服务层分页查询数据 */
        baseService.findByPage("/order/findByPage",page,
            rows,$scope.OrderEntity)
            .then(function(response){
                $scope.dataList = response.data.rows;
                /** 更新总记录数 */
                $scope.paginationConf.totalItems = response.data.total;
            });
    };

    // 搜索方法
    $scope.search = function () {
        // 发送异步请求
        baseService.sendPost("/Search", $scope.OrderParam).then(function (response) {
            // 获取响应数据 response.data: {total : 100, rows : [{},{}]}
            $scope.resultMap = response.data;

            // 调用生成页码的方法
            initPageNums();
        });
    };

    /** 生成页码的方法 */
    var initPageNums = function () {
        // pageNums
        $scope.pageNums = [];
        // 开始页码
        var firstPage = 1;
        // 结束页码
        var lastPage = $scope.resultMap.totalPages;

        // 前面加点
        $scope.firstDot = true;
        // 后面加点
        $scope.lastDot = true;

        // 判断总页数是不是大于5
        if ($scope.resultMap.totalPages > 5){

            // 判断当前页码是否靠前面近些
            if ($scope.OrderParam.page <= 3){ // 前面
                lastPage = 5;
                $scope.firstDot = false;
            }else if($scope.OrderParam.page >= $scope.resultMap.totalPages - 3){ // 后面
                // 判断当前页码是否靠后面近些
                firstPage = $scope.resultMap.totalPages - 4;
                $scope.lastDot = false;
            }else{ // 中间
                firstPage = $scope.OrderParam.page - 2;
                lastPage = $scope.OrderParam.page + 2;
            }
        }else {
            $scope.firstDot = false;
            $scope.lastDot = false;
        }

        // 循环生成5个页码
        for (var i = firstPage; i <= lastPage; i++){
            $scope.pageNums.push(i);
        }
    };

});
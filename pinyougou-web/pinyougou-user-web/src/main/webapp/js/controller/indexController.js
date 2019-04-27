/** 定义控制器层 */
app.controller('indexController', function($scope,$sce,$location,$filter,baseService){


    /** 定义获取登录用户名方法 */
    $scope.showName = function(){
        baseService.sendGet("/user/showName")
            .then(function(response){
                $scope.loginName = response.data.loginName;
            });
    };
    $scope.order = function () {
        baseService.sendGet("/order").then(function (response) {
            for(var i=0;i<response.data.length;i++){
                response.data[i].orderId=BigInt(response.data[i].orderId)
            }
            // $scope.aa=arr
            $scope.qweqwe = response.data;

        })

    };
    // 封装搜索参数的json对象
    $scope.searchParam = {page : 1, rows : 2};
    $scope.fenye = function () {
        baseService.sendPost("/findByPage",$scope.searchParam).then(function (response) {
            $scope.resultMap = response.data;

            var totalPage = 0;//总页数
            var num = $scope.resultMap.total;//表格所有行数(所有记录数)
            var pageSize = 2;//每页显示行数
            //总共分几页
            if(num/pageSize > parseInt(num/pageSize)){
                totalPage=parseInt(num/pageSize)+1;
            }else{
                totalPage=parseInt(num/pageSize);
            }
            $scope.totalPages = totalPage;
            initPageNums();
        })
    };

    // 根据页码查询
    $scope.pageSearch = function (page) {
        console.log(page)
        page = parseInt(page);
        // 判断页码的有效性: 不能小于1、不能大于总页数、不能等于当前页码
        if (page >= 1 && page <= $scope.totalPages
            && page != $scope.searchParam.page){
            // 当前页码
            $scope.searchParam.page = page;
            // 跳转的页码
            $scope.jumpPage = page;
            // 执行搜索
            $scope.fenye();
        }
    };

    /** 生成页码的方法 */
    var initPageNums = function () {
        // pageNums
        $scope.pageNums = [];

        // 开始页码
        var firstPage = 1;
        // 结束页码
        // var lastPage = $scope.resultMap.totalPages;

        var lastPage = $scope.totalPages;

        // 前面加点
        $scope.firstDot = true;
        // 后面加点
        $scope.lastDot = true;

        // 判断总页数是不是大于5
        if ($scope.totalPages > 5){
            // 判断当前页码是否靠前面近些
            if ($scope.searchParam.page <= 3){ // 前面
                lastPage = 5;
                $scope.firstDot = false;
            }else if($scope.searchParam.page >= $scope.totalPages - 3){ // 后面
                // 判断当前页码是否靠后面近些
                firstPage = $scope.totalPages - 4;
                $scope.lastDot = false;
            }else{ // 中间
                firstPage = $scope.searchParam.page - 2;
                lastPage = $scope.searchParam.page + 2;
            }
        }else {
            $scope.firstDot = false;
            $scope.lastDot = false;
        }
        // 循环生成页码

        for (var i = firstPage; i <= lastPage; i++){
            $scope.pageNums.push(i);
        }
    };

});


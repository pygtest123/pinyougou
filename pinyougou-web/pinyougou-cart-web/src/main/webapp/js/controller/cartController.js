// 定义购物车的控制器
app.controller('cartController', function ($scope,$controller,baseService) {

    // 继承baseController
    $controller('baseController', {$scope : $scope});
    $scope.findCart = function () {
        baseService.sendGet("/cart/findCart").then(function(response){
            // 获取响应数据
            $scope.carts = response.data;
            // 定义json对象封装统计的数据
            $scope.totalEntity = {totalNum : 0, totalMoney : 0};
            // 迭代用户的购物车集合
            for (var i = 0; i < $scope.carts.length; i++){
                // 获取商家的购物车
                $scope.cart = $scope.carts[i];
                // 迭代商家购物车中的商品
                for (var j = 0; j < $scope.cart.orderItems.length; j++){
                    // 获取购买的商品
                    var orderItem = $scope.cart.orderItems[j];
                    // 统计购买数量
                    $scope.totalEntity.totalNum += orderItem.num;
                    // 统计总金额
                    $scope.totalEntity.totalMoney += orderItem.totalFee;
                }
            }
        });
    };

    // 添加商品到购物车
    $scope.addCart = function (itemId, num) {
        baseService.sendGet("/cart/addCart?itemId="
            + itemId + "&num=" + num).then(function(response){
            // 获取响应数据
            if (response.data){
                // 重新查询购物车
                $scope.findCart();
            }
        });
    };
    /**
     * 购物车
     * @type {Array}
     */
    $scope.allChecked = false;  //默认全部选中为true
    /**
     * 店铺全选或者取消
     * @param index
     */
    $scope.shopChecked = function (index) {
        //alert('商店索引ID：' +index);
        $scope.totalEntity = {totalNum:0,totalMoney:0};
        $scope.orderItems = $scope.carts[index].orderItems;
        //alert(JSON.stringify(orderItems));
        var itemsLength = $scope.orderItems.length;
        if ($scope.carts[index].checked) {
            for (var i =  0; i < itemsLength; i++){
                //alert(itemsLength);
                $scope.orderItems[i].checked = true;
            }
        }else {
            for (var i = 0; i < itemsLength; i++){
                $scope.orderItems[i].checked = false;
            }
        }
        for (var i = 0; i < $scope.carts.length; i++) {
            // 获取商家的购物车
            var cart = $scope.carts[i];
            // 迭代商家购物车中的商品
            for (var j = 0; j < cart.orderItems.length; j++) {
                // 获取购买的商品
                var orderItem = cart.orderItems[j];
                if (orderItem.checked) {
                    // 统计购买数量
                    $scope.totalEntity.totalNum += orderItem.num;
                    // 统计总金额
                    $scope.totalEntity.totalMoney += orderItem.totalFee * orderItem.num;
                }
            }
        }
    };

    /**
     * 购物车全选
     */
    $scope.chooseAll = function () {
        $scope.totalEntity = {totalNum: 0, totalMoney: 0};
        for (var i = 0; i< $scope.carts.length; i++) {   //第一层for循环控制店铺全选按钮
            $scope.carts[i].checked = $scope.allChecked;
            $scope.orderItems = $scope.carts[i].orderItems;
            for (var c = 0; c < $scope.orderItems.length; c++){  //第二层for循环控制商品项全选按钮
                //alert(orderItems.length);
                $scope.orderItems[c].checked = $scope.allChecked;
            }
        }
        for (var i = 0;i < $scope.carts.length; i++){
            var cart = $scope.carts[i];
            $scope.cart = cart;
            for (var j = 0 ; j < cart.orderItems.length; j++){
                var orderItem = cart.orderItems[j];
                $scope.orderItem = orderItem;
                if($scope.allChecked){
                    $scope.totalEntity.totalNum += orderItem.num;
                    $scope.totalEntity.totalMoney += orderItem.totalFee * orderItem.num;
                }
            }
        }
    };

    /**
     * 商品项目选择
     * @type {boolean}
     */
    $scope.updateItemSelect = function ($event,shopIndex,itemIndex) {
        // 定义json对象封装统计的数据
        $scope.totalEntity = {totalNum: 0, totalMoney: 0};
        var cart = $scope.carts[shopIndex];
        var len = $scope.carts[shopIndex].orderItems.length;
        var count = 0;
        for (var i = 0; i < len; i++) {
            orderItem = cart.orderItems[i];
            if (orderItem.checked) count+=1;
        }
        if (count == len) {
            cart.checked = true;
        } else {
            cart.checked = false;
        }

        for (var i = 0; i < $scope.carts.length; i++) {
            // 获取商家的购物车
            var cart = $scope.carts[i];
            // 迭代商家购物车中的商品
            for (var j = 0; j < cart.orderItems.length; j++) {
                // 获取购买的商品
                var orderItem = cart.orderItems[j];
                if (orderItem.checked) {
                    // 统计购买数量
                    $scope.totalEntity.totalNum += orderItem.num;
                    // 统计总金额
                    $scope.totalEntity.totalMoney += orderItem.totalFee * orderItem.num;
                }

            }
        }
        var items = $scope.carts[index].orderItems;
        var itemsLength = items.length;
        if ($scope.carts[index].checked) {
            for (var i = 0; i < itemsLength; i++) {
                items[i].checked = false
            }
        } else {
            for (var i = 0; i < itemsLength; i++) {
                items[i].checked = true
            }
        }
    };
});
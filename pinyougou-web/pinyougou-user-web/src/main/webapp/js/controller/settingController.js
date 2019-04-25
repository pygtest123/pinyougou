/** 定义控制器层 */
app.controller('settingController', function ($scope, $controller, $http, baseService) {

    /** 指定继承indexController */
    $controller('indexController', {$scope: $scope});

    // 定义user对象
    $scope.user = {};


    $scope.updatePassword = function () {
        // 判断两次密码是否一致
        if ($scope.user.okPassword && $scope.user.newPassword == $scope.user.okPassword) {
            // $http.post()发送异步请求
            $http.post("/user/updatePassword", {
                "userName": $scope.user.userName,
                "newPassword": $scope.user.newPassword
            })
                .then(function (response) { // 请求成功
                    if (response.data) {
                        // 清空表单数据
                        $scope.user = {};
                        $scope.user.userName = "";
                        $scope.user.newPassword = "";
                        $scope.user.okPassword = "";
                       location.href="#two";
                        alert("成功")

                    } else {
                        alert("密码设置失败！");
                    }
                });
        } else {
            alert("两次密码不一致！");
        }

    }

    // 获取用户信息
    $scope.UserInfo= function () {
        baseService.sendPost("/user/UserInfo?userName=" +  $scope.loginName)
            .then(function(response){
                $scope.entity = response.data;
            });
    };

    // 定义发送短信验证码方法
    $scope.sendCode = function () {
        if ($scope.user.phone){
            baseService.sendPost("/user/sendCode?phone=" + $scope.user.phone)
                .then(function (response) {
                alert(response.data ? "发送成功!" : "发送失败!");
            });
        } else {
            alert("手机号码不正确!")
        }

    };


});
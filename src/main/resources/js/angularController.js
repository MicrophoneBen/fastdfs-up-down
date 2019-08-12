$scope.uploadFile = function () {
    uploadService.upload().success(function (result) {
        if (result.flag) {
            $scope.imgUrl = result.message;
        } else {
            alert(result.message);
        }
    })
}
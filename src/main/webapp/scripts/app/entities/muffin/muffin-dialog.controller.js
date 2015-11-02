'use strict';

angular.module('expressOApp').controller('MuffinDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Muffin',
        function($scope, $stateParams, $modalInstance, entity, Muffin) {

        $scope.muffin = entity;
        $scope.load = function(id) {
            Muffin.get({id : id}, function(result) {
                $scope.muffin = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('expressOApp:muffinUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.muffin.id != null) {
                Muffin.update($scope.muffin, onSaveFinished);
            } else {
                Muffin.save($scope.muffin, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

'use strict';

angular.module('expressOApp').controller('CoffeeOrderDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'CoffeeOrder', 'Recipe',
        function($scope, $stateParams, $modalInstance, entity, CoffeeOrder, Recipe) {

        $scope.coffeeOrder = entity;
        $scope.recipes = Recipe.query();
        $scope.load = function(id) {
            CoffeeOrder.get({id : id}, function(result) {
                $scope.coffeeOrder = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('expressOApp:coffeeOrderUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.coffeeOrder.id != null) {
                CoffeeOrder.update($scope.coffeeOrder, onSaveFinished);
            } else {
                CoffeeOrder.save($scope.coffeeOrder, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

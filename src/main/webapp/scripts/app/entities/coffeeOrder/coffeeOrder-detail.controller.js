'use strict';

angular.module('expressOApp')
    .controller('CoffeeOrderDetailController', function ($scope, $rootScope, $stateParams, entity, CoffeeOrder, Recipe) {
        $scope.coffeeOrder = entity;
        $scope.load = function (id) {
            CoffeeOrder.get({id: id}, function(result) {
                $scope.coffeeOrder = result;
            });
        };
        var unsubscribe = $rootScope.$on('expressOApp:coffeeOrderUpdate', function(event, result) {
            $scope.coffeeOrder = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

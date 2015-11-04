'use strict';

angular.module('expressOApp')
    .controller('CategoryDetailController', function ($scope, $rootScope, $stateParams, entity, Category) {
        $scope.category = entity;
        $scope.load = function (id) {
            Category.get({id: id}, function(result) {
                $scope.category = result;
            });
        };
        var unsubscribe = $rootScope.$on('expressOApp:categoryUpdate', function(event, result) {
            $scope.category = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

'use strict';

angular.module('expressOApp')
    .controller('MuffinDetailController', function ($scope, $rootScope, $stateParams, entity, Muffin, Allergen) {
        $scope.muffin = entity;
        $scope.load = function (id) {
            Muffin.get({id: id}, function(result) {
                $scope.muffin = result;
            });
        };
        var unsubscribe = $rootScope.$on('expressOApp:muffinUpdate', function(event, result) {
            $scope.muffin = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

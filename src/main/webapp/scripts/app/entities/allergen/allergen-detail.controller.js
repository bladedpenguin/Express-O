'use strict';

angular.module('expressOApp')
    .controller('AllergenDetailController', function ($scope, $rootScope, $stateParams, entity, Allergen, Muffin) {
        $scope.allergen = entity;
        $scope.load = function (id) {
            Allergen.get({id: id}, function(result) {
                $scope.allergen = result;
            });
        };
        var unsubscribe = $rootScope.$on('expressOApp:allergenUpdate', function(event, result) {
            $scope.allergen = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

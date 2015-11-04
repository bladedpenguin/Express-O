'use strict';

angular.module('expressOApp')
    .controller('IngredientQuantityDetailController', function ($scope, $rootScope, $stateParams, entity, IngredientQuantity, Recipe, Ingredient) {
        $scope.ingredientQuantity = entity;
        $scope.load = function (id) {
            IngredientQuantity.get({id: id}, function(result) {
                $scope.ingredientQuantity = result;
            });
        };
        var unsubscribe = $rootScope.$on('expressOApp:ingredientQuantityUpdate', function(event, result) {
            $scope.ingredientQuantity = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

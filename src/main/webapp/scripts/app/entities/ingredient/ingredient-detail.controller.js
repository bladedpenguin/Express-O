'use strict';

angular.module('expressOApp')
    .controller('IngredientDetailController', function ($scope, $rootScope, $stateParams, entity, Ingredient, Recipe) {
        $scope.ingredient = entity;
        $scope.load = function (id) {
            Ingredient.get({id: id}, function(result) {
                $scope.ingredient = result;
            });
        };
        var unsubscribe = $rootScope.$on('expressOApp:ingredientUpdate', function(event, result) {
            $scope.ingredient = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

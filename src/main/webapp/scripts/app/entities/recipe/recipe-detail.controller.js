'use strict';

angular.module('expressOApp')
    .controller('RecipeDetailController', function ($scope, $rootScope, $stateParams, entity, Recipe, Ingredient) {
        $scope.recipe = entity;
        $scope.load = function (id) {
            Recipe.get({id: id}, function(result) {
                $scope.recipe = result;
            });
        };
        var unsubscribe = $rootScope.$on('expressOApp:recipeUpdate', function(event, result) {
            $scope.recipe = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

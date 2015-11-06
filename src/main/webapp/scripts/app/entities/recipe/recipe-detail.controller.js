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
        $scope.totalCost = function(recipe){
            var t = 0;
            for (var i = 0; i<recipe.ingredients.length; i++){
                console.log(recipe.ingredients[i]);
                var n = recipe.ingredients[i].ingredient.cost * recipe.ingredients[i].quantity;
                if (n !== null && !isNaN(n)){
                    t += n;
                }
            }
            console.log("total: " + t);
            return t;
        };
    });

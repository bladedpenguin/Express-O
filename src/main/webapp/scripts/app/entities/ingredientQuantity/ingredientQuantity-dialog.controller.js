'use strict';

angular.module('expressOApp').controller('IngredientQuantityDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'IngredientQuantity', 'Recipe', 'Ingredient',
        function($scope, $stateParams, $modalInstance, entity, IngredientQuantity, Recipe, Ingredient) {

        $scope.ingredientQuantity = entity;
        $scope.recipes = Recipe.query();
        $scope.ingredients = Ingredient.query();
        $scope.load = function(id) {
            IngredientQuantity.get({id : id}, function(result) {
                $scope.ingredientQuantity = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('expressOApp:ingredientQuantityUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.ingredientQuantity.id != null) {
                IngredientQuantity.update($scope.ingredientQuantity, onSaveFinished);
            } else {
                IngredientQuantity.save($scope.ingredientQuantity, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

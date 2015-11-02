'use strict';

angular.module('expressOApp').controller('IngredientDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Ingredient', 'Recipe',
        function($scope, $stateParams, $modalInstance, entity, Ingredient, Recipe) {

        $scope.ingredient = entity;
        $scope.recipes = Recipe.query();
        $scope.load = function(id) {
            Ingredient.get({id : id}, function(result) {
                $scope.ingredient = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('expressOApp:ingredientUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.ingredient.id != null) {
                Ingredient.update($scope.ingredient, onSaveFinished);
            } else {
                Ingredient.save($scope.ingredient, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

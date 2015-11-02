'use strict';

angular.module('expressOApp').controller('RecipeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Recipe',
        function($scope, $stateParams, $modalInstance, entity, Recipe) {

        $scope.recipe = entity;
        $scope.load = function(id) {
            Recipe.get({id : id}, function(result) {
                $scope.recipe = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('expressOApp:recipeUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.recipe.id != null) {
                Recipe.update($scope.recipe, onSaveFinished);
            } else {
                Recipe.save($scope.recipe, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
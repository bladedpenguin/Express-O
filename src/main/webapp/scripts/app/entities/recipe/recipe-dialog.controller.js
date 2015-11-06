'use strict';

angular.module('expressOApp').controller('RecipeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Recipe', 'Ingredient', 'IngredientQuantity',
        function($scope, $stateParams, $modalInstance, entity, Recipe, Ingredient, IngredientQuantity) {

        $scope.recipe = entity;
        $scope.ingredients = Ingredient.query();
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
            //console.log('saving: ' + JSON.stringify($scope.recipe));
//            for (var i=0; i<$scope.recipe.ingredients.length;i++){
//                //$scope.recipe.ingredients[i].recipe = $scope.recipe.id;
//                $scope.iqSave($scope.recipe.ingredients[i]);
//            }
            if ($scope.recipe.id !== null) {
                Recipe.update($scope.recipe, onSaveFinished);
            } else {
                Recipe.save($scope.recipe, onSaveFinished);
            }
            
        };
        $scope.delete = function (id, index) {
            console.log("delete:" + index);
            $scope.recipe.ingredients.splice(index,1);
            IngredientQuantity.delete({id: id},
                function () {
                    $scope.loadAll();
                    $scope.clear();
                });
        };
        $scope.iqSave = function (iq) {
            if (iq.id !== null) {
                IngredientQuantity.update(iq);
            } else {
                IngredientQuantity.save(iq);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

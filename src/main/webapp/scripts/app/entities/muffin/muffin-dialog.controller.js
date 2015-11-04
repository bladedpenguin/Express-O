'use strict';

angular.module('expressOApp').controller('MuffinDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Muffin', 'Allergen', 'Category',
        function($scope, $stateParams, $modalInstance, entity, Muffin, Allergen, Category) {

        $scope.muffin = entity;
        $scope.allergens = Allergen.query();
        $scope.categories = Category.query();
        $scope.load = function(id) {
            Muffin.get({id : id}, function(result) {
                $scope.muffin = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('expressOApp:muffinUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.muffin.id != null) {
                Muffin.update($scope.muffin, onSaveFinished);
            } else {
                Muffin.save($scope.muffin, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

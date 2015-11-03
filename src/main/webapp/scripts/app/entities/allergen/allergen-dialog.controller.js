'use strict';

angular.module('expressOApp').controller('AllergenDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Allergen', 'Muffin',
        function($scope, $stateParams, $modalInstance, entity, Allergen, Muffin) {

        $scope.allergen = entity;
        $scope.muffins = Muffin.query();
        $scope.load = function(id) {
            Allergen.get({id : id}, function(result) {
                $scope.allergen = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('expressOApp:allergenUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.allergen.id != null) {
                Allergen.update($scope.allergen, onSaveFinished);
            } else {
                Allergen.save($scope.allergen, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

'use strict';

angular.module('expressOApp').controller('AllergyDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Allergy', 'Muffin',
        function($scope, $stateParams, $modalInstance, entity, Allergy, Muffin) {

        $scope.allergy = entity;
        $scope.muffins = Muffin.query();
        $scope.load = function(id) {
            Allergy.get({id : id}, function(result) {
                $scope.allergy = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('expressOApp:allergyUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.allergy.id != null) {
                Allergy.update($scope.allergy, onSaveFinished);
            } else {
                Allergy.save($scope.allergy, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

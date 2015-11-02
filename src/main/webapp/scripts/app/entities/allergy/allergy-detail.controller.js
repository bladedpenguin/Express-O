'use strict';

angular.module('expressOApp')
    .controller('AllergyDetailController', function ($scope, $rootScope, $stateParams, entity, Allergy, Muffin) {
        $scope.allergy = entity;
        $scope.load = function (id) {
            Allergy.get({id: id}, function(result) {
                $scope.allergy = result;
            });
        };
        var unsubscribe = $rootScope.$on('expressOApp:allergyUpdate', function(event, result) {
            $scope.allergy = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

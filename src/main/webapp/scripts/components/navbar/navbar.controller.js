'use strict';

angular.module('expressOApp')
    .controller('NavbarController', function ($scope, $location, $state, Auth, Principal, ENV) {
        //$scope.isAuthenticated = Principal.isAuthenticated;
        $scope.isAuthenticated = function(){return true;};
        $scope.$state = $state;
        $scope.inProduction = ENV === 'prod';

        $scope.logout = function () {
            Auth.logout();
            $state.go('home');
        };
    });

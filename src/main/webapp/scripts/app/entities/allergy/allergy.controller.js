'use strict';

angular.module('expressOApp')
    .controller('AllergyController', function ($scope, Allergy, ParseLinks) {
        $scope.allergys = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Allergy.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.allergys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Allergy.get({id: id}, function(result) {
                $scope.allergy = result;
                $('#deleteAllergyConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Allergy.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAllergyConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.allergy = {
                name: null,
                id: null
            };
        };
    });

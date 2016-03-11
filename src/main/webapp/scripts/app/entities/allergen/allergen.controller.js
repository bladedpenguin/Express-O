'use strict';

angular.module('expressOApp')
    .controller('AllergenController', function ($scope, Allergen, ParseLinks) {
        $scope.allergens = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Allergen.query({page: $scope.page, size: 20}, function(result, headers) {
                //$scope.links = ParseLinks.parse(headers('link'));
                $scope.allergens = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Allergen.get({id: id}, function(result) {
                $scope.allergen = result;
                $('#deleteAllergenConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Allergen.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAllergenConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.allergen = {
                name: null,
                id: null
            };
        };
    });

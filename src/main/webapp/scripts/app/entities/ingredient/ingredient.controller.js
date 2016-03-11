'use strict';

angular.module('expressOApp')
    .controller('IngredientController', function ($scope, Ingredient, ParseLinks) {
        $scope.ingredients = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Ingredient.query({page: $scope.page, size: 20}, function(result, headers) {
                //$scope.links = ParseLinks.parse(headers('link'));
                $scope.ingredients = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Ingredient.get({id: id}, function(result) {
                $scope.ingredient = result;
                $('#deleteIngredientConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Ingredient.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteIngredientConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.ingredient = {
                name: null,
                cost: null,
                unit: null,
                id: null
            };
        };
    });

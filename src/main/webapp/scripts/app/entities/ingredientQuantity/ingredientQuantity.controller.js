'use strict';

angular.module('expressOApp')
    .controller('IngredientQuantityController', function ($scope, IngredientQuantity, ParseLinks) {
        $scope.ingredientQuantitys = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            IngredientQuantity.query({page: $scope.page, size: 20}, function(result, headers) {
                //$scope.links = ParseLinks.parse(headers('link'));
                $scope.ingredientQuantitys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            IngredientQuantity.get({id: id}, function(result) {
                $scope.ingredientQuantity = result;
                $('#deleteIngredientQuantityConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            IngredientQuantity.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteIngredientQuantityConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.ingredientQuantity = {
                quantity: null,
                id: null
            };
        };
    });

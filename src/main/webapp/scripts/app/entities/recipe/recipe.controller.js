'use strict';

angular.module('expressOApp')
    .controller('RecipeController', function ($scope, Recipe, ParseLinks) {
        $scope.recipes = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Recipe.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.recipes = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Recipe.get({id: id}, function(result) {
                $scope.recipe = result;
                $('#deleteRecipeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Recipe.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteRecipeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.recipe = {
                name: null,
                id: null
            };
        };
    });

'use strict';

angular.module('expressOApp')
    .controller('MuffinController', function ($scope, Muffin, ParseLinks) {
        $scope.muffins = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Muffin.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.muffins = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Muffin.get({id: id}, function(result) {
                $scope.muffin = result;
                $('#deleteMuffinConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Muffin.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteMuffinConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.muffin = {
                name: null,
                cost: null,
                vendor: null,
                id: null
            };
        };
    });

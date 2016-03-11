'use strict';

angular.module('expressOApp')
    .controller('CoffeeOrderController', function ($scope, CoffeeOrder, ParseLinks) {
        $scope.coffeeOrders = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            CoffeeOrder.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.coffeeOrders = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            CoffeeOrder.get({id: id}, function(result) {
                $scope.coffeeOrder = result;
                $('#deleteCoffeeOrderConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            CoffeeOrder.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCoffeeOrderConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.coffeeOrder = {
                quantity: null,
                customerName: null,
                id: null
            };
        };
    });

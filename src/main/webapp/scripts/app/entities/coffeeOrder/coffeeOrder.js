'use strict';

angular.module('expressOApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('coffeeOrder', {
                parent: 'entity',
                url: '/coffeeOrders',
                data: {
                    authorities: [],
                    pageTitle: 'CoffeeOrders'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/coffeeOrder/coffeeOrders.html',
                        controller: 'CoffeeOrderController'
                    }
                },
                resolve: {
                }
            })
            .state('coffeeOrder.detail', {
                parent: 'entity',
                url: '/coffeeOrder/{id}',
                data: {
                    authorities: [],
                    pageTitle: 'CoffeeOrder'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/coffeeOrder/coffeeOrder-detail.html',
                        controller: 'CoffeeOrderDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'CoffeeOrder', function($stateParams, CoffeeOrder) {
                        return CoffeeOrder.get({id : $stateParams.id});
                    }]
                }
            })
            .state('coffeeOrder.new', {
                parent: 'coffeeOrder',
                url: '/new',
                data: {
                    authorities: [],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/coffeeOrder/coffeeOrder-dialog.html',
                        controller: 'CoffeeOrderDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    quantity: null,
                                    customerName: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('coffeeOrder', null, { reload: true });
                    }, function() {
                        $state.go('coffeeOrder');
                    })
                }]
            })
            .state('coffeeOrder.edit', {
                parent: 'coffeeOrder',
                url: '/{id}/edit',
                data: {
                    authorities: [],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/coffeeOrder/coffeeOrder-dialog.html',
                        controller: 'CoffeeOrderDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['CoffeeOrder', function(CoffeeOrder) {
                                return CoffeeOrder.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('coffeeOrder', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

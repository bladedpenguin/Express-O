'use strict';

angular.module('expressOApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ingredientQuantity', {
                parent: 'entity',
                url: '/ingredientQuantitys',
                data: {
                    authorities: [ ],
                    pageTitle: 'IngredientQuantitys'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ingredientQuantity/ingredientQuantitys.html',
                        controller: 'IngredientQuantityController'
                    }
                },
                resolve: {
                }
            })
            .state('ingredientQuantity.detail', {
                parent: 'entity',
                url: '/ingredientQuantity/{id}',
                data: {
                    authorities: [ ],
                    pageTitle: 'IngredientQuantity'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ingredientQuantity/ingredientQuantity-detail.html',
                        controller: 'IngredientQuantityDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'IngredientQuantity', function($stateParams, IngredientQuantity) {
                        return IngredientQuantity.get({id : $stateParams.id});
                    }]
                }
            })
            .state('ingredientQuantity.new', {
                parent: 'ingredientQuantity',
                url: '/new',
                data: {
                    authorities: [ ],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/ingredientQuantity/ingredientQuantity-dialog.html',
                        controller: 'IngredientQuantityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    quantity: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('ingredientQuantity', null, { reload: true });
                    }, function() {
                        $state.go('ingredientQuantity');
                    })
                }]
            })
            .state('ingredientQuantity.edit', {
                parent: 'ingredientQuantity',
                url: '/{id}/edit',
                data: {
                    authorities: [ ],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/ingredientQuantity/ingredientQuantity-dialog.html',
                        controller: 'IngredientQuantityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['IngredientQuantity', function(IngredientQuantity) {
                                return IngredientQuantity.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('ingredientQuantity', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

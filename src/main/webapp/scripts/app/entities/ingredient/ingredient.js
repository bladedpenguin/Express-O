'use strict';

angular.module('expressOApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ingredient', {
                parent: 'entity',
                url: '/ingredients',
                data: {
                    authorities: [ ],
                    pageTitle: 'Ingredients'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ingredient/ingredients.html',
                        controller: 'IngredientController'
                    }
                },
                resolve: {
                }
            })
            .state('ingredient.detail', {
                parent: 'entity',
                url: '/ingredient/{id}',
                data: {
                    authorities: [ ],
                    pageTitle: 'Ingredient'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ingredient/ingredient-detail.html',
                        controller: 'IngredientDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Ingredient', function($stateParams, Ingredient) {
                        return Ingredient.get({id : $stateParams.id});
                    }]
                }
            })
            .state('ingredient.new', {
                parent: 'ingredient',
                url: '/new',
                data: {
                    authorities: [ ],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/ingredient/ingredient-dialog.html',
                        controller: 'IngredientDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    cost: null,
                                    unit: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('ingredient', null, { reload: true });
                    }, function() {
                        $state.go('ingredient');
                    })
                }]
            })
            .state('ingredient.edit', {
                parent: 'ingredient',
                url: '/{id}/edit',
                data: {
                    authorities: [ ],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/ingredient/ingredient-dialog.html',
                        controller: 'IngredientDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Ingredient', function(Ingredient) {
                                return Ingredient.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('ingredient', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

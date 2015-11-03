'use strict';

angular.module('expressOApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('recipe', {
                parent: 'entity',
                url: '/recipes',
                data: {
                    authorities: [ ],
                    pageTitle: 'Recipes'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/recipe/recipes.html',
                        controller: 'RecipeController'
                    }
                },
                resolve: {
                }
            })
            .state('recipe.detail', {
                parent: 'entity',
                url: '/recipe/{id}',
                data: {
                    authorities: [ ],
                    pageTitle: 'Recipe'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/recipe/recipe-detail.html',
                        controller: 'RecipeDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Recipe', function($stateParams, Recipe) {
                        return Recipe.get({id : $stateParams.id});
                    }]
                }
            })
            .state('recipe.new', {
                parent: 'recipe',
                url: '/new',
                data: {
                    authorities: [ ],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/recipe/recipe-dialog.html',
                        controller: 'RecipeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('recipe', null, { reload: true });
                    }, function() {
                        $state.go('recipe');
                    })
                }]
            })
            .state('recipe.edit', {
                parent: 'recipe',
                url: '/{id}/edit',
                data: {
                    authorities: [ ],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/recipe/recipe-dialog.html',
                        controller: 'RecipeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Recipe', function(Recipe) {
                                return Recipe.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('recipe', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

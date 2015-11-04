'use strict';

angular.module('expressOApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('category', {
                parent: 'entity',
                url: '/categorys',
                data: {
                    authorities: [ ],
                    pageTitle: 'Categorys'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/category/categorys.html',
                        controller: 'CategoryController'
                    }
                },
                resolve: {
                }
            })
            .state('category.detail', {
                parent: 'entity',
                url: '/category/{id}',
                data: {
                    authorities: [ ],
                    pageTitle: 'Category'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/category/category-detail.html',
                        controller: 'CategoryDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Category', function($stateParams, Category) {
                        return Category.get({id : $stateParams.id});
                    }]
                }
            })
            .state('category.new', {
                parent: 'category',
                url: '/new',
                data: {
                    authorities: [ ],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/category/category-dialog.html',
                        controller: 'CategoryDialogController',
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
                        $state.go('category', null, { reload: true });
                    }, function() {
                        $state.go('category');
                    })
                }]
            })
            .state('category.edit', {
                parent: 'category',
                url: '/{id}/edit',
                data: {
                    authorities: [ ],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/category/category-dialog.html',
                        controller: 'CategoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Category', function(Category) {
                                return Category.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('category', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

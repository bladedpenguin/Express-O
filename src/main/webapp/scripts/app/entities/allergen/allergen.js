'use strict';

angular.module('expressOApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('allergen', {
                parent: 'entity',
                url: '/allergens',
                data: {
                    authorities: [ ],
                    pageTitle: 'Allergens'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/allergen/allergens.html',
                        controller: 'AllergenController'
                    }
                },
                resolve: {
                }
            })
            .state('allergen.detail', {
                parent: 'entity',
                url: '/allergen/{id}',
                data: {
                    authorities: [ ],
                    pageTitle: 'Allergen'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/allergen/allergen-detail.html',
                        controller: 'AllergenDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Allergen', function($stateParams, Allergen) {
                        return Allergen.get({id : $stateParams.id});
                    }]
                }
            })
            .state('allergen.new', {
                parent: 'allergen',
                url: '/new',
                data: {
                    authorities: [ ],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/allergen/allergen-dialog.html',
                        controller: 'AllergenDialogController',
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
                        $state.go('allergen', null, { reload: true });
                    }, function() {
                        $state.go('allergen');
                    })
                }]
            })
            .state('allergen.edit', {
                parent: 'allergen',
                url: '/{id}/edit',
                data: {
                    authorities: [ ],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/allergen/allergen-dialog.html',
                        controller: 'AllergenDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Allergen', function(Allergen) {
                                return Allergen.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('allergen', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

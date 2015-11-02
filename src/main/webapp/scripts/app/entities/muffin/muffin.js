'use strict';

angular.module('expressOApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('muffin', {
                parent: 'entity',
                url: '/muffins',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Muffins'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/muffin/muffins.html',
                        controller: 'MuffinController'
                    }
                },
                resolve: {
                }
            })
            .state('muffin.detail', {
                parent: 'entity',
                url: '/muffin/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Muffin'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/muffin/muffin-detail.html',
                        controller: 'MuffinDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Muffin', function($stateParams, Muffin) {
                        return Muffin.get({id : $stateParams.id});
                    }]
                }
            })
            .state('muffin.new', {
                parent: 'muffin',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/muffin/muffin-dialog.html',
                        controller: 'MuffinDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    cost: null,
                                    vendor: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('muffin', null, { reload: true });
                    }, function() {
                        $state.go('muffin');
                    })
                }]
            })
            .state('muffin.edit', {
                parent: 'muffin',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/muffin/muffin-dialog.html',
                        controller: 'MuffinDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Muffin', function(Muffin) {
                                return Muffin.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('muffin', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

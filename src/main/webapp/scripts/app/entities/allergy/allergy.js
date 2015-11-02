'use strict';

angular.module('expressOApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('allergy', {
                parent: 'entity',
                url: '/allergys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Allergys'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/allergy/allergys.html',
                        controller: 'AllergyController'
                    }
                },
                resolve: {
                }
            })
            .state('allergy.detail', {
                parent: 'entity',
                url: '/allergy/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Allergy'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/allergy/allergy-detail.html',
                        controller: 'AllergyDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Allergy', function($stateParams, Allergy) {
                        return Allergy.get({id : $stateParams.id});
                    }]
                }
            })
            .state('allergy.new', {
                parent: 'allergy',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/allergy/allergy-dialog.html',
                        controller: 'AllergyDialogController',
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
                        $state.go('allergy', null, { reload: true });
                    }, function() {
                        $state.go('allergy');
                    })
                }]
            })
            .state('allergy.edit', {
                parent: 'allergy',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/allergy/allergy-dialog.html',
                        controller: 'AllergyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Allergy', function(Allergy) {
                                return Allergy.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('allergy', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

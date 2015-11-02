'use strict';

angular.module('expressOApp')
    .factory('Allergy', function ($resource, DateUtils) {
        return $resource('api/allergys/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });

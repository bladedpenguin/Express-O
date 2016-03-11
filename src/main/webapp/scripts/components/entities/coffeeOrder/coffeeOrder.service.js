'use strict';

angular.module('expressOApp')
    .factory('CoffeeOrder', function ($resource, DateUtils) {
        return $resource('api/coffeeOrders/:id', {}, {
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

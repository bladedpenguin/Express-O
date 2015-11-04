'use strict';

angular.module('expressOApp')
    .factory('IngredientQuantity', function ($resource, DateUtils) {
        return $resource('api/ingredientQuantitys/:id', {}, {
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

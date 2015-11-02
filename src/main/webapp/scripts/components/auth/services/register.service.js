'use strict';

angular.module('expressOApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });



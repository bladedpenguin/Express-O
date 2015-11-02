 'use strict';

angular.module('expressOApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-expressOApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-expressOApp-params')});
                }
                return response;
            }
        };
    });

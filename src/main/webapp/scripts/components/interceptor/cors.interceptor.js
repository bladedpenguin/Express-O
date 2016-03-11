'use strict'; 

//this wants to make sure api calls go to the api server.
angular.module('expressOApp')
    .factory('corsInterceptor', function ($rootScope, $q, $injector, localStorageService) {
        return { 
            request: function(request) {
                if (request.url.indexOf('api') >=0){
                    console.log("rewriting api url: "  + request.url);
                 //   request.url = 'http://ec2-52-87-152-103.compute-1.amazonaws.com:8080/express-o-0.0.1-SNAPSHOT/' + request.url;
                }
                return request;
            }
        };
    });
//$httpProvider.interceptors.push('corsInterceptor');
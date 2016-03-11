'use strict';

describe('CoffeeOrder Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockCoffeeOrder, MockRecipe;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockCoffeeOrder = jasmine.createSpy('MockCoffeeOrder');
        MockRecipe = jasmine.createSpy('MockRecipe');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'CoffeeOrder': MockCoffeeOrder,
            'Recipe': MockRecipe
        };
        createController = function() {
            $injector.get('$controller')("CoffeeOrderDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'expressOApp:coffeeOrderUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

'use strict';

describe('Muffin Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockMuffin, MockAllergen;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockMuffin = jasmine.createSpy('MockMuffin');
        MockAllergen = jasmine.createSpy('MockAllergen');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Muffin': MockMuffin,
            'Allergen': MockAllergen
        };
        createController = function() {
            $injector.get('$controller')("MuffinDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'expressOApp:muffinUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

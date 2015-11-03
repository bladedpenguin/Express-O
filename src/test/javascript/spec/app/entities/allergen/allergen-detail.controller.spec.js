'use strict';

describe('Allergen Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockAllergen, MockMuffin;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockAllergen = jasmine.createSpy('MockAllergen');
        MockMuffin = jasmine.createSpy('MockMuffin');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Allergen': MockAllergen,
            'Muffin': MockMuffin
        };
        createController = function() {
            $injector.get('$controller')("AllergenDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'expressOApp:allergenUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

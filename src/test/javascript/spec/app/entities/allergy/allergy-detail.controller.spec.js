'use strict';

describe('Allergy Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockAllergy, MockMuffin;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockAllergy = jasmine.createSpy('MockAllergy');
        MockMuffin = jasmine.createSpy('MockMuffin');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Allergy': MockAllergy,
            'Muffin': MockMuffin
        };
        createController = function() {
            $injector.get('$controller')("AllergyDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'expressOApp:allergyUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

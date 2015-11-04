'use strict';

describe('IngredientQuantity Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockIngredientQuantity, MockRecipe, MockIngredient;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockIngredientQuantity = jasmine.createSpy('MockIngredientQuantity');
        MockRecipe = jasmine.createSpy('MockRecipe');
        MockIngredient = jasmine.createSpy('MockIngredient');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'IngredientQuantity': MockIngredientQuantity,
            'Recipe': MockRecipe,
            'Ingredient': MockIngredient
        };
        createController = function() {
            $injector.get('$controller')("IngredientQuantityDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'expressOApp:ingredientQuantityUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

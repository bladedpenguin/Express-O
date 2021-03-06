'use strict';

describe('Recipe Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockRecipe, MockIngredient;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockRecipe = jasmine.createSpy('MockRecipe');
        MockIngredient = jasmine.createSpy('MockIngredient');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Recipe': MockRecipe,
            'Ingredient': MockIngredient
        };
        createController = function() {
            $injector.get('$controller')("RecipeDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'expressOApp:recipeUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
